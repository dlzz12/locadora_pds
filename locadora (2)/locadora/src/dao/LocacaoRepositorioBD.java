package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Locacao;

public class LocacaoRepositorioBD {
    private static final DateTimeFormatter FORMATO =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static boolean colunaUsuarioVerificada = false;

    private static synchronized void garantirColunaUsuario() throws SQLException {
        if (colunaUsuarioVerificada) {
            return;
        }

        String sql = "SELECT COUNT(*) FROM information_schema.COLUMNS "
                   + "WHERE TABLE_SCHEMA = DATABASE() "
                   + "AND TABLE_NAME = 'Locacao' "
                   + "AND COLUMN_NAME = 'usuario'";

        try (
            Connection connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql)
        ) {
            result.next();

            if (result.getInt(1) == 0) {
                statement.execute(
                    "ALTER TABLE Locacao ADD COLUMN usuario VARCHAR(50)"
                );
            }
        }

        colunaUsuarioVerificada = true;
    }

    public void salvar(Locacao locacao) throws SQLException {
        garantirColunaUsuario();

        String erro = locacao.validar();

        if (erro != null) {
            throw new IllegalArgumentException(erro);
        }

        String dono = buscarDono(locacao.getRenavam());

        if (dono != null && dono.equalsIgnoreCase(locacao.getUsuario())) {
            throw new IllegalArgumentException(
                "Você não pode locar um carro que você mesmo cadastrou."
            );
        }

        Locacao atual = buscarLocacaoAtiva(locacao.getRenavam());

        if (atual != null) {
            throw new IllegalArgumentException(
                "Este carro já foi locado (de "
                + atual.getDataInicio().format(FORMATO)
                + " a "
                + atual.getDataTermino().format(FORMATO)
                + ")."
            );
        }

        String sql = "INSERT INTO Locacao (dataInicio, dataTermino, renavam, usuario) "
                   + "VALUES (?, ?, ?, ?)";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(locacao.getDataInicio()));
            statement.setDate(2, Date.valueOf(locacao.getDataTermino()));
            statement.setInt(3, locacao.getRenavam());
            statement.setString(4, locacao.getUsuario());

            statement.executeUpdate();
        }
    }

    public Set<Integer> listarRenavamsLocados() throws SQLException {
        String sql = "SELECT DISTINCT renavam FROM Locacao WHERE dataTermino >= CURDATE()";

        Set<Integer> renavams = new HashSet<>();

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {
                renavams.add(result.getInt("renavam"));
            }
        }

        return renavams;
    }

    public Locacao buscarLocacaoAtiva(int renavam) throws SQLException {
        garantirColunaUsuario();

        String sql = "SELECT dataInicio, dataTermino, renavam, usuario FROM Locacao "
                   + "WHERE renavam = ? AND dataTermino >= CURDATE() "
                   + "ORDER BY dataInicio LIMIT 1";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, renavam);

            List<Locacao> locacoes = lerLocacoes(statement);

            return locacoes.isEmpty() ? null : locacoes.get(0);
        }
    }

    private String buscarDono(int renavam) throws SQLException {
        String sql = "SELECT usuario FROM Carro WHERE renavam = ?";

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, renavam);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getString("usuario") : null;
            }
        }
    }

    public List<String[]> listarDoUsuario(String usuario) throws SQLException {
        garantirColunaUsuario();

        String sql = "SELECT c.marca, c.modelo, c.placa, l.dataInicio, l.dataTermino "
                   + "FROM Locacao l JOIN Carro c ON c.renavam = l.renavam "
                   + "WHERE l.usuario = ? ORDER BY l.dataInicio";

        List<String[]> lista = new ArrayList<>();

        try (
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, usuario);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    lista.add(new String[] {
                        result.getString("marca"),
                        result.getString("modelo"),
                        result.getString("placa"),
                        result.getDate("dataInicio").toLocalDate().format(FORMATO),
                        result.getDate("dataTermino").toLocalDate().format(FORMATO)
                    });
                }
            }
        }

        return lista;
    }

    private List<Locacao> lerLocacoes(PreparedStatement statement) throws SQLException {
        List<Locacao> locacoes = new ArrayList<>();

        try (ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                locacoes.add(
                    new Locacao(
                        result.getDate("dataInicio").toLocalDate(),
                        result.getDate("dataTermino").toLocalDate(),
                        result.getInt("renavam"),
                        result.getString("usuario")
                    )
                );
            }
        }

        return locacoes;
    }
}
