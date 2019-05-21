package dev.volkovs.office.tool.invoice.generator.lv;

import dev.volkovs.office.tool.invoice.model.AbstractModelConverter;
import dev.volkovs.office.tool.invoice.model.spi.ModelConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LvModelConverter extends AbstractModelConverter implements ModelConverter {

    @Override
    public String convertAmount(BigDecimal amount) {
        if (amount == null) {
            return null;
        }

        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(' ');
        return new DecimalFormat("#,##0.00", decimalFormatSymbols).format(amount);
    }

    @Override
    public String convertCount(BigDecimal amount) {
        if (amount == null) {
            return null;
        }

        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        return new DecimalFormat("#.#", decimalFormatSymbols).format(amount);
    }

    @Override
    public String convert(LocalDate date) {
        if (date == null) {
            return null;
        }

        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }
}
