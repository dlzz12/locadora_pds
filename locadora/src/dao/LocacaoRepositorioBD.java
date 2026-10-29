package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.Locacao;

public class LocacaoRepositorioBD {

    private static final DateTimeFormatter FORMATO =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Evita verificar a estrutura da tabela mais de uma vez
    private static boolean colunaUsuarioVerificada = false;

    // =========================================================
    // GARANTE A COLUNA "usuario" (QUEM LOCOU) NA TABELA Locacao
    // =========================================================

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

    // =========================================================
    // SALVAR
    // =========================================================

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

    // =========================================================
    // LOCAÇÃO EM ANDAMENTO OU AGENDADA DO CARRO (SE HOUVER)
    // =========================================================

    private Locacao buscarLocacaoAtiva(int renavam) throws SQLException {

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

    // =========================================================
    // USUÁRIO QUE CADASTROU O CARRO
    // =========================================================

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

    // =========================================================
    // TRANSFORMA AS LINHAS DO BANCO EM OBJETOS Locacao
    // =========================================================

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
