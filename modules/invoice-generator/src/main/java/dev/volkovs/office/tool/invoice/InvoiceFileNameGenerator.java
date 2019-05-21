package dev.volkovs.office.tool.invoice;

import com.google.common.annotations.VisibleForTesting;
import dev.volkovs.office.tool.invoice.model.Invoice;

import static java.lang.String.format;

public class InvoiceFileNameGenerator {

    public String generate(Invoice invoice) {
        String invoiceNumber = invoice.getNumber().replace("/", ":");
        String workingMonth = invoice.getDate().minusMonths(1).getMonth().name();
        String provider = shortName(invoice.getCompanyAgreement().getServiceProvider().getName());
        String consumer = shortName(invoice.getCompanyAgreement().getServiceConsumer().getName());
        return format("%s_%s-Invoice-%s_%s.docx", invoiceNumber, workingMonth, provider, consumer);
    }

    @VisibleForTesting
    String shortName(String companyName) {
        return companyName.split("\\s")[0];
    }

}
