package br.com.roadtowork.dao;

import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.Meta;
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
public class MetaDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(Meta meta) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO T_META (" +
                            "id_meta, id_usuario, nr_meta_total, nr_atual, dt_criacao, dt_conclusao, st_concluida" +
                            ") VALUES (sq_t_meta.nextval, ?, ?, ?, ?, ?, ?)",
                    new String[]{"id_meta"}
            );

            stmt.setInt(1, meta.getUsuario().getId());
            stmt.setInt(2, meta.getMetaTotal());
            stmt.setInt(3, meta.getAtual());
            stmt.setObject(4, meta.getDataCriacao());
            stmt.setObject(5, meta.getDataConclusao());
            stmt.setInt(6, meta.isConcluida() ? 1 : 0);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                meta.setId(rs.getInt(1));
            }
        }
    }

    public List<Meta> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT id_meta, id_usuario, nr_meta_total, nr_atual, dt_criacao, dt_conclusao, st_concluida " +
                            "FROM T_META"
            );

            ResultSet rs = stmt.executeQuery();
            List<Meta> metas = new ArrayList<>();

            while (rs.next()) {
                metas.add(parseMeta(rs));
            }

            return metas;
        }
    }

    public Meta buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT id_meta, id_usuario, nr_meta_total, nr_atual, st_concluida, dt_criacao, dt_conclusao " +
                            "FROM T_META WHERE id_meta = ?"
            );

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Meta não encontrada");
            }

            return parseMeta(rs);
        }
    }

    public Meta buscarPorUsuario(int idUsuario) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT * FROM T_META WHERE ID_USUARIO = ?"
            );
            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Meta não encontrada para este usuário");
            }
            return parseMeta(rs);
        }
    }

    public void atualizar(Meta meta)
            throws SQLException, EntidadeNaoEncontradaException {

        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_META SET nr_meta_total = ?, nr_atual = ?, dt_criacao = ?, dt_conclusao = ?, st_concluida = ? " +
                            "WHERE id_meta = ?"
            );

            stmt.setInt(1, meta.getMetaTotal());
            stmt.setInt(2, meta.getAtual());
            stmt.setObject(3, meta.getDataCriacao());
            stmt.setObject(4, meta.getDataConclusao());
            stmt.setInt(5, meta.isConcluida() ? 1 : 0);
            stmt.setInt(6, meta.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Meta não encontrada para atualizar.");
            }
        }
    }

    public void incrementar(int idUsuario)
            throws SQLException, EntidadeNaoEncontradaException {

        try (Connection conexao = dataSource.getConnection()) {

            // incrementa
            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_META " +
                            "SET nr_atual = nr_atual + 1 " +
                            "WHERE id_usuario = ? AND st_concluida = 0"
            );

            stmt.setInt(1, idUsuario);

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Meta não encontrada ou já concluída.");
            }

            // verifica se bateu a meta
            PreparedStatement stmtCheck = conexao.prepareStatement(
                    "UPDATE T_META " +
                            "SET st_concluida = 1, dt_conclusao = SYSDATE " +
                            "WHERE id_usuario = ? AND nr_atual >= nr_meta_total"
            );

            stmtCheck.setInt(1, idUsuario);
            stmtCheck.executeUpdate();
        }
    }

    public void atualizarStatus(Meta meta) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_META SET st_concluida = ?, dt_conclusao = ? WHERE id_meta = ?"
            );

            stmt.setInt(1, meta.isConcluida() ? 1 : 0);
            stmt.setObject(2, meta.getDataConclusao());
            stmt.setInt(3, meta.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new EntidadeNaoEncontradaException("Meta não encontrada para atualizar status");
            }
        }
    }

    public void apagar(int idMeta) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "DELETE FROM T_META WHERE id_meta = ?"
            );

            stmt.setInt(1, idMeta);

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Meta não encontrada para apagar.");
            }
        }
    }

    private Meta parseMeta(ResultSet rs) throws SQLException {

        int id = rs.getInt("id_meta");
        int idUsuario = rs.getInt("id_usuario");
        int metaTotal = rs.getInt("nr_meta_total");
        int atual = rs.getInt("nr_atual");
        LocalDate dtCriacao = rs.getObject("dt_criacao", LocalDate.class);
        LocalDate dtConclusao = rs.getObject("dt_conclusao", LocalDate.class);
        boolean concluida = rs.getInt("st_concluida") == 1;

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Meta meta = new Meta();
        meta.setId(id);
        meta.setMetaTotal(metaTotal);
        meta.setAtual(atual);
        meta.setDataCriacao(dtCriacao);
        meta.setDataConclusao(dtConclusao);
        meta.setConcluida(concluida);
        meta.setUsuario(usuario);

        return meta;
    }
}
