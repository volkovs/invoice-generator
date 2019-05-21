package dev.volkovs.office.tool.invoice;

import dev.volkovs.office.tool.invoice.model.Company;
import dev.volkovs.office.tool.invoice.model.CompanyAgreement;
import dev.volkovs.office.tool.invoice.model.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceFileNameGeneratorTest {

    private InvoiceFileNameGenerator generator;
    private Invoice invoice;
    private Company provider;
    private Company consumer;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();
        provider = new Company();
        consumer = new Company();
        CompanyAgreement companyAgreement = new CompanyAgreement();
        companyAgreement.setServiceProvider(provider);
        companyAgreement.setServiceConsumer(consumer);
        invoice.setCompanyAgreement(companyAgreement);
        generator = new InvoiceFileNameGenerator();
    }

    @Test
    void generate() {
        invoice.setDate(LocalDate.of(2019, 4, 13));
        invoice.setNumber("RBK-55/2019");
        provider.setName("SP Consulting");
        consumer.setName("Receiver Ltd.");
        assertThat(generator.generate(invoice)).isEqualTo("RBK-55:2019_MARCH-Invoice-SP_Receiver.docx");
    }

    @Test
    void shortName() {
        assertThat(generator.shortName("B-Tails Consulting Ltd.")).isEqualTo("B-Tails");
        assertThat(generator.shortName("B-Tails")).isEqualTo("B-Tails");
    }
}