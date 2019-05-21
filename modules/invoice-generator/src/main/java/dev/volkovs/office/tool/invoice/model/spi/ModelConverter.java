package dev.volkovs.office.tool.invoice.model.spi;

import dev.volkovs.office.tool.invoice.model.Invoice;
import dev.volkovs.office.tool.invoice.model.InvoiceItem;
import dev.volkovs.office.tool.invoice.model.flat.Inv;
import dev.volkovs.office.tool.invoice.model.flat.InvIt;

public interface ModelConverter {

    Inv convert(Invoice invoice);

    InvIt convert(InvoiceItem item);

}
