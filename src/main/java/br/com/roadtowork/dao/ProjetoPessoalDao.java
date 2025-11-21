package br.com.roadtowork.dao;

import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.ProjetoPessoal;
import br.com.roadtowork.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class ProjetoPessoalDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(ProjetoPessoal projeto) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO T_PROJETO_PESSOAL " +
                            "(ID_PROJETO, ID_USUARIO, DT_CRIACAO, DS_TITULO, DS_DESCRICAO, ST_CONCLUIDO, DT_CONCLUSAO) " +
                            "VALUES (SQ_T_PROJETO_PESSOAL.NEXTVAL, ?, ?, ?, ?, ?, ?)",
                    new String[]{"ID_PROJETO"}
            );

            stmt.setInt(1, projeto.getUsuario().getId());
            stmt.setDate(2, java.sql.Date.valueOf(projeto.getDataCriacao())); // DT_CRIACAO
            stmt.setString(3, projeto.getTitulo());
            stmt.setString(4, projeto.getDescricao());
            stmt.setInt(5, projeto.isConcluido() ? 1 : 0);
            stmt.setDate(6, projeto.getDataConclusao() != null
                    ? java.sql.Date.valueOf(projeto.getDataConclusao())
                    : null);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                projeto.setId(rs.getInt(1));
            }
        }
    }

    public List<ProjetoPessoal> listarPorUsuario(int idUsuario) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT * FROM T_PROJETO_PESSOAL WHERE ID_USUARIO = ? ORDER BY DT_CRIACAO DESC"
            );

            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();
            List<ProjetoPessoal> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(parseProjeto(rs));
            }
            return lista;
        }
    }

    public ProjetoPessoal buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT * FROM T_PROJETO_PESSOAL WHERE ID_PROJETO = ?"
            );

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Projeto não encontrado");
            }

            return parseProjeto(rs);
        }
    }

    public void atualizar(ProjetoPessoal projeto) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_PROJETO_PESSOAL SET " +
                            "DS_TITULO = ?, DS_DESCRICAO = ?, ST_CONCLUIDO = ?, DT_CONCLUSAO = ? " +
                            "WHERE ID_PROJETO = ?"
            );

            stmt.setString(1, projeto.getTitulo());
            stmt.setString(2, projeto.getDescricao());
            stmt.setInt(3, projeto.isConcluido() ? 1 : 0);

            if (projeto.getDataConclusao() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(projeto.getDataConclusao()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setInt(5, projeto.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Projeto não existe para ser atualizado");
            }
        }
    }

        public void apagar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "DELETE FROM T_PROJETO_PESSOAL WHERE ID_PROJETO = ?"
            );

            stmt.setInt(1, id);

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Projeto não existe para ser removido");
            }
        }
    }

    public int contarConcluidos(int usuarioId) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT COUNT(*) FROM T_PROJETO_PESSOAL " +
                            "WHERE ID_USUARIO = ? AND ST_CONCLUIDO = 1"
            );

            stmt.setInt(1, usuarioId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        }
    }

    private ProjetoPessoal parseProjeto(ResultSet rs) throws SQLException {

        int id = rs.getInt("id_projeto");
        int idUsuario = rs.getInt("id_usuario");

        String titulo = rs.getString("ds_titulo");
        String descricao = rs.getString("ds_descricao");

        LocalDate dtCriacao = rs.getObject("dt_criacao", LocalDate.class); // CORRETO
        LocalDate dtConclusao = rs.getObject("dt_conclusao", LocalDate.class);

        boolean concluido = rs.getInt("st_concluido") == 1;

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        ProjetoPessoal projeto = new ProjetoPessoal();
        projeto.setId(id);
        projeto.setTitulo(titulo);
        projeto.setDescricao(descricao);
        projeto.setDataCriacao(dtCriacao);
        projeto.setDataConclusao(dtConclusao);
        projeto.setConcluido(concluido);
        projeto.setUsuario(usuario);

        return projeto;
    }
}