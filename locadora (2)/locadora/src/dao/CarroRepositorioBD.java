package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Carro;

public class CarroRepositorioBD {
    private static final String COLUNAS =
        "renavam, marca, modelo, ano, placa, usuario, precoDia";

    private static boolean colunasVerificadas = false;

    private static synchronized void garantirColunas() throws SQLException {
        if (colunasVerificadas) {
            return;
        }

        garantirColuna("usuario", "VARCHAR(50)");
        garantirColuna("precoDia", "DECIMAL(10,2) NOT NULL DEFAULT 0");

        colunasVerificadas = true;
    }

    private static void garantirColuna(String nome, String tipo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM information_schema.COLUMNS "
                   + "WHERE TABLE_SCHEMA = DATABASE() "
                   + "AND TABLE_NAME = 'Carro' "
                   + "AND COLUMN_NAME = '" + nome + "'";

        try (
            Connection connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql)
        ) {
            result.next();

            if (result.getInt(1) == 0) {
                statement.execute(
                    "ALTER TABLE Carro ADD COLUMN " + nome + " " + tipo
                );
            }
        }
    }

    public void salvar(Carro carro) throws SQLException {
        garantirColunas();

        String erro = carro.validar();

        if (erro != null) {
            throw new IllegalArgumentException(erro);
        }

        if (existe(carro.getPlaca())) {
            throw new IllegalArgumentException(
                "Já existe um carro cadastrado com essa placa."
            );
        }

        String sql = "INSERT INTO Carro (" + COLUNAS + ") "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, carro.getRenavam());
            statement.setString(2, carro.getMarca());
            statement.setString(3, carro.getModelo());
            statement.setInt(4, carro.getAno());
            statement.setString(5, carro.getPlaca());
            statement.setString(6, carro.getUsuario());
            statement.setBigDecimal(7, carro.getPrecoDia());

            statement.executeUpdate();
        }
    }

    public boolean existe(String placa) throws SQLException {
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT renavam FROM Carro WHERE placa = ?";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, placa);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private static final String NAO_LOCADO =
        "NOT EXISTS (SELECT 1 FROM Locacao l "
        + "WHERE l.renavam = Carro.renavam AND l.dataTermino >= CURDATE())";

    public List<Carro> listarDisponiveis() throws SQLException {
        garantirColunas();

        String sql = "SELECT " + COLUNAS + " FROM Carro "
                   + "WHERE " + NAO_LOCADO + " "
                   + "ORDER BY renavam";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            return lerCarros(statement);
        }
    }

    public List<Carro> listarPorUsuario(String usuario) throws SQLException {
        garantirColunas();

        String sql = "SELECT " + COLUNAS + " FROM Carro "
                   + "WHERE usuario = ? ORDER BY renavam";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usuario);

            return lerCarros(statement);
        }
    }

    public List<Carro> pesquisarDisponiveis(String termo) throws SQLException {
        garantirColunas();

        String sql = "SELECT " + COLUNAS + " FROM Carro "
                   + "WHERE " + NAO_LOCADO + " "
                   + "AND (CAST(renavam AS CHAR) LIKE ? "
                   + "OR marca LIKE ? "
                   + "OR modelo LIKE ? "
                   + "OR CAST(ano AS CHAR) LIKE ? "
                   + "OR placa LIKE ? "
                   + "OR usuario LIKE ?) "
                   + "ORDER BY marca, modelo";

        String filtro = "%" + termo.trim() + "%";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (int i = 1; i <= 6; i++) {
                statement.setString(i, filtro);
            }

            return lerCarros(statement);
        }
    }

    public int contar() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Carro";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            if (result.next()) {
                return result.getInt(1);
            }
        }

        return 0;
    }

    private List<Carro> lerCarros(PreparedStatement statement) throws SQLException {
        List<Carro> carros = new ArrayList<>();

        try (ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Carro carro = new Carro(
                    result.getInt("renavam"),
                    result.getString("marca"),
                    result.getString("modelo"),
                    result.getInt("ano"),
                    result.getString("placa")
                );

                carro.setUsuario(
                    result.getString("usuario")
                );

                carro.setPrecoDia(
                    result.getBigDecimal("precoDia")
                );

                carros.add(carro);
            }
        }

        return carros;
    }
}
