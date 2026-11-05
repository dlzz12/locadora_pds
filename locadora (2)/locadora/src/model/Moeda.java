package model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Moeda {
    private static final String SEM_MILHAR = "\\d+([.,]\\d{1,2})?";
    private static final String COM_MILHAR = "\\d{1,3}(\\.\\d{3})+(,\\d{1,2})?";

    public static String formatar(BigDecimal valor) {
        return NumberFormat
            .getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
            .format(valor);
    }

    public static BigDecimal converter(String texto) {
        String valor = texto.trim();

        if (valor.matches(COM_MILHAR)) {
            valor = valor.replace(".", "");
        } else if (!valor.matches(SEM_MILHAR)) {
            throw new NumberFormatException(texto);
        }

        return new BigDecimal(valor.replace(",", "."));
    }
}
