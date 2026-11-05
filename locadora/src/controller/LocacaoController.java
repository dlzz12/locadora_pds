package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import View.Locacao_Carro;
import dao.LocacaoRepositorioBD;
import model.Locacao;

public class LocacaoController {
    private static final DateTimeFormatter FORMATO =
        DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private Locacao_Carro view;
    private LocacaoRepositorioBD repositorio;

    public LocacaoController(Locacao_Carro view) {
        this.view = view;
        this.repositorio = new LocacaoRepositorioBD();
    }

    public boolean locar() {
        try {
            LocalDate inicio = converterData(view.getDataInicio(), "início");
            LocalDate termino = converterData(view.getDataTermino(), "término");

            Locacao locacao = new Locacao(
                inicio,
                termino,
                view.getRenavam(),
                view.getUsuarioLogado()
            );

            repositorio.salvar(locacao);

            view.mostrarMensagem(
                "Carro locado com sucesso!\n"
                + "Período: " + inicio.format(FORMATO)
                + " a " + termino.format(FORMATO)
            );

            return true;
        } catch (IllegalArgumentException e) {
            view.mostrarMensagem(
                e.getMessage()
            );
        } catch (SQLException e) {
            view.mostrarMensagem(
                "Erro ao acessar o banco de dados:\n"
                + e.getMessage()
            );
        }

        return false;
    }

    private LocalDate converterData(String texto, String nomeCampo) {
        if (texto == null || texto.contains("_") || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Preencha a data de " + nomeCampo + " (dd/mm/aaaa)."
            );
        }

        try {
            return LocalDate.parse(texto.trim(), FORMATO);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Data de " + nomeCampo + " inválida: " + texto
            );
        }
    }

    public static List<String[]> meusCarros(String usuario) throws SQLException {
        return new LocacaoRepositorioBD().listarDoUsuario(usuario);
    }

    public static String formatar(LocalDate data) {
        return data.format(FORMATO);
    }
}
