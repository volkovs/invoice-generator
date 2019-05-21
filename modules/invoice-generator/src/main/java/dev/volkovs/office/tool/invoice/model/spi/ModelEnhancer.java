package dev.volkovs.office.tool.invoice.model.spi;

import dev.volkovs.office.tool.invoice.model.Invoice;

public interface ModelEnhancer {

    void enhance(Invoice model);

}
