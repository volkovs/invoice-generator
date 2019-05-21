package dev.volkovs.office.tool.invoice.example;

import com.google.common.annotations.VisibleForTesting;
import dev.volkovs.office.tool.invoice.model.Invoice;
import dev.volkovs.office.tool.invoice.model.InvoiceItem;
import dev.volkovs.office.tool.invoice.model.spi.LocaleProvider;
import dev.volkovs.office.tool.invoice.model.spi.ModelEnhancer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class ModelEnhancerExample implements ModelEnhancer {

    private final Locale locale;

    public ModelEnhancerExample() {
        Iterator<LocaleProvider> locales = ServiceLoader.load(LocaleProvider.class).iterator();
        if (locales.hasNext()) {
            this.locale = locales.next().get();
        } else {
            this.locale = Locale.getDefault();
        }
    }


    @Override
    public void enhance(Invoice model) {
        List<InvoiceItem> items = model.getItems();
        if (items.size() != 1) {
            return;
        }

        InvoiceItem item = items.iterator().next();
        if (!isEmpty(item.getName())) {
            return;
        }

        item.setName(generateItemName(model.getDate()));
    }

    @VisibleForTesting
    String generateItemName(LocalDate date) {
        LocalDate previousMonth = date.minusMonths(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'. g. 'MMM", locale);
        return String.format("IT pakalpojumi (%s)", formatter.format(previousMonth));
    }

}
