package controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import View.Locacao_Carro;
import dao.LocacaoRepositorioBD;
import model.Locacao;
import model.Moeda;

public class LocacaoController {
    public static final String ENDERECO_LOCADORA =
        "Av. dos Automóveis, 1500 - Centro, São Paulo - SP";

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
                "Locação confirmada!\n\n"
                + "Carro: " + view.getDescricaoCarro() + "\n"
                + "Período: " + inicio.format(FORMATO)
                + " a " + termino.format(FORMATO)
                + " (" + textoDias(locacao.getDias()) + ")\n"
                + "Preço por dia: " + Moeda.formatar(view.getPrecoDia()) + "\n"
                + "Valor total: " + Moeda.formatar(calcularTotal(view.getPrecoDia(), locacao.getDias())) + "\n\n"
                + "Retire o carro em:\n"
                + ENDERECO_LOCADORA
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

    public static String resumirTotal(String textoInicio, String textoTermino, BigDecimal precoDia) {
        try {
            Locacao locacao = new Locacao(
                LocalDate.parse(textoInicio.trim(), FORMATO),
                LocalDate.parse(textoTermino.trim(), FORMATO),
                0,
                null
            );

            if (locacao.getDias() < 1) {
                return "Valor total: informe as datas";
            }

            return "Valor total: "
                + Moeda.formatar(calcularTotal(precoDia, locacao.getDias()))
                + " (" + textoDias(locacao.getDias()) + ")";
        } catch (DateTimeParseException e) {
            return "Valor total: informe as datas";
        }
    }

    public static BigDecimal calcularTotal(BigDecimal precoDia, long dias) {
        return precoDia.multiply(BigDecimal.valueOf(dias));
    }

    public static String textoDias(long dias) {
        return dias == 1 ? "1 dia" : dias + " dias";
    }

    public static List<String[]> meusCarros(String usuario) throws SQLException {
        return new LocacaoRepositorioBD().listarDoUsuario(usuario);
    }

    public static String formatar(LocalDate data) {
        return data.format(FORMATO);
    }
}
