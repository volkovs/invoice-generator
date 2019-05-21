package dev.volkovs.office.tool.invoice;

import dev.volkovs.office.tool.invoice.model.CompanyAgreement;
import dev.volkovs.office.tool.invoice.model.Invoice;
import dev.volkovs.office.tool.invoice.model.InvoiceItem;
import dev.volkovs.office.tool.invoice.model.input.InvoiceInput;
import dev.volkovs.office.tool.invoice.model.input.Settings;
import dev.volkovs.office.tool.invoice.model.spi.InvoiceNumberSequence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ServiceLoader;
import java.util.function.Function;

import static java.lang.String.format;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.toList;

public class ModelGenerator {

    private InvoiceInput input;
    private Settings settings;
    private InvoiceNumberSequence sequence;

    public ModelGenerator(InvoiceInput input, Settings settings) {
        this.input = input;
        this.settings = settings;
        sequence = ServiceLoader.load(InvoiceNumberSequence.class).iterator().next();
    }

    public Invoice generate() {

        CompanyAgreement companyAgreement = new CompanyAgreement();
        companyAgreement.setServiceProvider(settings.getSender());
        String companyToCharge = input.getCompanyToCharge();
        String agreementNumber = settings.findAgreementNumberByCompanyName(companyToCharge)
                .orElseThrow(() -> new RuntimeException(format("Company %s is not registered in settings", companyToCharge)));
        companyAgreement.setAgreementNumber(agreementNumber);
        companyAgreement.setServiceConsumer(settings.getAgreements().get(agreementNumber));

        Invoice invoice = new Invoice();
        invoice.setCompanyAgreement(companyAgreement);
        invoice.setDate(LocalDate.now());
        invoice.setDue(LocalDate.now().with(lastDayOfMonth()));
        invoice.setNumber(sequence.next());
        invoice.setItems(input.getItems().stream()
                .map(item -> InvoiceItem.valueOf(item, settings))
                .collect(toList()));

        invoice.setTotalAmountBeforeDiscountAndTax(sum(invoice, InvoiceItem::getSubTotal));
        invoice.setTotalDiscountAmount(sum(invoice, InvoiceItem::getTotalDiscountAmount));
        invoice.setTotalAmountBeforeTax(sum(invoice, InvoiceItem::getTotalAmountBeforeTax));
        invoice.setTotalTaxAmount(sum(invoice, InvoiceItem::getTotalTaxAmount));
        invoice.setTotalToBePaid(sum(invoice, InvoiceItem::getTotalToBePaid));

        // TODO:
        invoice.setTotalAmountWords("Divi tukstoši seši simti deviņdesmit eiro 22 centi");

        return invoice;
    }

    private BigDecimal sum(Invoice invoice, Function<InvoiceItem, BigDecimal> field) {
        return invoice.getItems().stream().map(field).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
