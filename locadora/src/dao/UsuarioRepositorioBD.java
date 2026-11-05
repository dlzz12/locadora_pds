package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Usuario;

public class UsuarioRepositorioBD {
    public UsuarioRepositorioBD() throws SQLException {
        criarTabela();
    }

    private void criarTabela() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Usuario ("
                   + " id INT PRIMARY KEY AUTO_INCREMENT,"
                   + " usuario VARCHAR(50) NOT NULL UNIQUE,"
                   + " senha VARCHAR(64) NOT NULL,"
                   + " tipo VARCHAR(10) NOT NULL DEFAULT 'cliente'"
                   + ")";

        try (
            Connection connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);

            try {
                statement.execute(
                    "ALTER TABLE Usuario ADD COLUMN tipo VARCHAR(10) NOT NULL DEFAULT 'cliente'"
                );
            } catch (SQLException e) {
            }

            statement.execute(
                "INSERT IGNORE INTO Usuario (usuario, senha, tipo) VALUES ('admin', '"
                + gerarHash("admin") + "', 'operador')"
            );
        }
    }

    public String buscarTipo(String usuario) throws SQLException {
        String sql = "SELECT tipo FROM Usuario WHERE usuario = ?";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usuario.trim());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getString("tipo");
                }

                return "cliente";
            }
        }
    }

    public void salvar(Usuario usuario) throws SQLException {
        String erro = usuario.validar();

        if (erro != null) {
            throw new IllegalArgumentException(erro);
        }

        if (existe(usuario.getUsuario())) {
            throw new IllegalArgumentException(
                "Já existe um usuário cadastrado com esse nome."
            );
        }

        String sql = "INSERT INTO Usuario (usuario, senha) VALUES (?, ?)";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usuario.getUsuario().trim());
            statement.setString(2, gerarHash(usuario.getSenha()));

            statement.executeUpdate();
        }
    }

    public boolean existe(String usuario) throws SQLException {
        String sql = "SELECT id FROM Usuario WHERE usuario = ?";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usuario.trim());

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    public boolean autenticar(String usuario, String senha) throws SQLException {
        String sql = "SELECT senha FROM Usuario WHERE usuario = ?";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usuario.trim());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return false;
                }

                return result.getString("senha").equals(gerarHash(senha));
            }
        }
    }

    private String gerarHash(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] bytes = digest.digest(
                senha.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hex = new StringBuilder();

            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
