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
import java.util.List;

@ApplicationScoped
public class ProjetoPessoalDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(ProjetoPessoal projeto) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO T_PROJETO_PESSOAL (" +
                            "id_projeto, id_usuario, dt_periodo, ds_titulo, ds_descricao, st_concluido, dt_conclusao" +
                            ") VALUES (sq_t_projeto_pessoal.nextval, ?, ?, ?, ?, ?, ?)",
                    new String[]{"id_projeto"}
            );

            stmt.setInt(1, projeto.getUsuario().getId());
            stmt.setObject(2, projeto.getPeriodo());
            stmt.setString(3, projeto.getTitulo());
            stmt.setString(4, projeto.getDescricao());
            stmt.setInt(5, projeto.isConcluido() ? 1 : 0); // boolean → número
            stmt.setObject(6, projeto.getConclusao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                projeto.setId(rs.getInt(1));
            }
        }
    }

    public List<ProjetoPessoal> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT id_projeto, id_usuario, dt_periodo, ds_titulo, ds_descricao, st_concluido, dt_conclusao " +
                            "FROM T_PROJETO_PESSOAL"
            );

            ResultSet rs = stmt.executeQuery();
            List<ProjetoPessoal> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(parseProjeto(rs));
            }

            return lista;
        }
    }

    public ProjetoPessoal buscar(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT id_projeto, id_usuario, dt_periodo, ds_titulo, ds_descricao, st_concluido, dt_conclusao " +
                            "FROM T_PROJETO_PESSOAL WHERE id_projeto = ?"
            );
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Projeto pessoal não encontrado");
            }

            return parseProjeto(rs);
        }
    }

    public void atualizarStatus(ProjetoPessoal projeto)
            throws SQLException, EntidadeNaoEncontradaException {

        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_PROJETO_PESSOAL SET st_concluido = ?, dt_conclusao = ? WHERE id_projeto = ?"
            );

            stmt.setInt(1, projeto.isConcluido() ? 1 : 0);
            stmt.setObject(2, projeto.getConclusao());
            stmt.setInt(3, projeto.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Projeto pessoal não existe para ser atualizado");
            }
        }
    }

    public void atualizar(ProjetoPessoal projeto) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE t_projeto_pessoal " +
                            "SET dt_periodo = ?, ds_titulo = ?, ds_descricao = ? " +
                            "WHERE id_projeto = ?"
            );

            stmt.setObject(1, projeto.getPeriodo()); // LocalDate mapeia para DATE
            stmt.setString(2, projeto.getTitulo());
            stmt.setString(3, projeto.getDescricao());
            stmt.setInt(4, projeto.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new EntidadeNaoEncontradaException("Projeto não encontrado para atualizar");
            }
        }
    }

    public void apagar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "DELETE FROM t_projeto_pessoal WHERE id_projeto = ?"
            );

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new EntidadeNaoEncontradaException("Projeto não encontrado para apagar");
            }
        }
    }

    private ProjetoPessoal parseProjeto(ResultSet rs) throws SQLException {

        int id = rs.getInt("id_projeto");
        int idUsuario = rs.getInt("id_usuario");
        LocalDate periodo = rs.getObject("dt_periodo", LocalDate.class);
        String titulo = rs.getString("ds_titulo");
        String descricao = rs.getString("ds_descricao");
        boolean concluido = rs.getInt("st_concluido") == 1;
        LocalDate dtConclusao = rs.getObject("dt_conclusao", LocalDate.class);

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        return new ProjetoPessoal(
                id,
                periodo,
                titulo,
                descricao,
                concluido,
                dtConclusao,
                usuario
        );
    }
}
