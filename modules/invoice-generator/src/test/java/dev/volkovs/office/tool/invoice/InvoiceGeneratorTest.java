package dev.volkovs.office.tool.invoice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

class InvoiceGeneratorTest {

    private InvoiceGenerator invoiceGenerator;

    @BeforeEach
    void setUp() {
        invoiceGenerator = new InvoiceGenerator();
    }

    @Test
    @Disabled("Manual testing")
    void convertToPdf() {
        invoiceGenerator.convertToPdf(new File("XXX-6:2019_APRIL-Invoice-Provider_Consumer.docx"));
    }

}