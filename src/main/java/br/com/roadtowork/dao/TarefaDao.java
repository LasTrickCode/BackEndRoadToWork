package br.com.roadtowork.dao;

import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
import br.com.roadtowork.model.Tarefa;
import br.com.roadtowork.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TarefaDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(Tarefa tarefa) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO T_TAREFA (ID_TAREFA, ID_USUARIO, DS_TITULO, DS_DESCRICAO, ST_CONCLUIDA) " +
                            "VALUES (SQ_T_TAREFA.NEXTVAL, ?, ?, ?, 0)",
                    new String[]{"ID_TAREFA"}
            );

            stmt.setInt(1, tarefa.getUsuario().getId());
            stmt.setString(2, tarefa.getTitulo());
            stmt.setString(3, tarefa.getDescricao());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                tarefa.setId(rs.getInt(1));
            }
        }
    }

    public List<Tarefa> listarPorUsuario(int id) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT * FROM T_TAREFA WHERE ID_USUARIO = ?"
            );

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            List<Tarefa> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(parseTarefa(rs));
            }
            return lista;
        }
    }

    public Tarefa buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT * FROM T_TAREFA WHERE ID_TAREFA = ?"
            );
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Tarefa não encontrada");
            }

            return parseTarefa(rs);
        }
    }

    public void atualizar(Tarefa tarefa) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_TAREFA SET DS_TITULO = ?, DS_DESCRICAO = ?, ST_CONCLUIDA = ? WHERE ID_TAREFA = ?"
            );

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.isConcluido() ? 1 : 0);
            stmt.setInt(4, tarefa.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Tarefa não existe para ser atualizada");
            }
        }
    }

    public void apagar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "DELETE FROM T_TAREFA WHERE ID_TAREFA = ?"
            );

            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Tarefa não existe para ser removida");
            }
        }
    }

    public void marcarComoConcluida(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_TAREFA SET ST_CONCLUIDA = 1 WHERE ID_TAREFA = ?"
            );

            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Tarefa não encontrada para marcar como concluída");
            }

            // OBS: Aqui você pode chamar o Service para registrar histórico
            // e atualizar a meta mensal, não faça no DAO.
        }
    }

    private Tarefa parseTarefa(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID_TAREFA");
        int idUsuario = rs.getInt("ID_USUARIO");
        String titulo = rs.getString("DS_TITULO");
        String descricao = rs.getString("DS_DESCRICAO");
        boolean concluido = rs.getInt("ST_CONCLUIDA") == 1;

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        return new Tarefa(id, titulo, descricao, concluido, usuario);
    }
}
