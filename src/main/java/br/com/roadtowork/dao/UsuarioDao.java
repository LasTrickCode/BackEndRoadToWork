package br.com.roadtowork.dao;

import br.com.roadtowork.exception.EntidadeNaoEncontradaException;
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
public class UsuarioDao {

    @Inject
    private DataSource dataSource;

    public void cadastrar(Usuario usuario) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO T_USUARIO (ID_USUARIO, NM_USUARIO, NM_EMAIL, DS_SENHA) " +
                            "VALUES (SQ_T_USUARIO.NEXTVAL, ?, ?, ?)",
                    new String[] {"ID_USUARIO"}
            );

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
        }
    }

    public List<Usuario> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM T_USUARIO");
            ResultSet rs = stmt.executeQuery();

            List<Usuario> usuarios = new ArrayList<>();
            while (rs.next()) {
                usuarios.add(parseUsuario(rs));
            }

            return usuarios;
        }
    }

    public Usuario buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT * FROM T_USUARIO WHERE ID_USUARIO = ?"
            );
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Usuário não encontrado");
            }

            return parseUsuario(rs);
        }
    }

    public void atualizar(Usuario usuario) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_USUARIO SET NM_USUARIO = ?, NM_EMAIL = ?, DS_SENHA = ? " +
                            "WHERE ID_USUARIO = ?"
            );

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getId());

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não existe para ser atualizado");
            }
        }
    }

    public void apagar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "DELETE FROM T_USUARIO WHERE ID_USUARIO = ?"
            );

            stmt.setInt(1, id);

            if (stmt.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não existe para ser removido");
            }
        }
    }

    public Usuario login(String email, String senha) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT ID_USUARIO, NM_USUARIO, NM_EMAIL, DS_SENHA " +
                            "FROM T_USUARIO WHERE NM_EMAIL = ? AND DS_SENHA = ?"
            );

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EntidadeNaoEncontradaException("Email ou senha inválidos");
            }

            return parseUsuario(rs);
        }
    }

    private Usuario parseUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID_USUARIO");
        String nome = rs.getString("NM_USUARIO");
        String email = rs.getString("NM_EMAIL");
        String senha = rs.getString("DS_SENHA");

        return new Usuario(id, nome, email, senha);
    }
}
