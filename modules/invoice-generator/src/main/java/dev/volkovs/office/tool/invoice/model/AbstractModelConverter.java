package dev.volkovs.office.tool.invoice.model;

import dev.volkovs.office.tool.invoice.model.flat.Inv;
import dev.volkovs.office.tool.invoice.model.flat.InvIt;
import dev.volkovs.office.tool.invoice.model.spi.ModelConverter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.stream.Collectors.toList;

public abstract class AbstractModelConverter implements ModelConverter {

    public abstract String convertAmount(BigDecimal amount);

    public abstract String convertCount(BigDecimal amount);

    public abstract String convert(LocalDate date);

    @Override
    public Inv convert(Invoice invoice) {
        Inv inv = new Inv();
        inv.setDate(convert(invoice.getDate()));
        inv.setDue(convert(invoice.getDue()));
        inv.setNumber(invoice.getNumber());
        inv.setFromName(invoice.getCompanyAgreement().getServiceProvider().getName());
        inv.setFromRegNum(invoice.getCompanyAgreement().getServiceProvider().getRegistrationNumber());
        inv.setFromTaxNum(invoice.getCompanyAgreement().getServiceProvider().getTaxPayerNumber());
        inv.setFromAddr(invoice.getCompanyAgreement().getServiceProvider().getAddress());
        inv.setFromPhone(invoice.getCompanyAgreement().getServiceProvider().getPhoneNumber());
        inv.setFromBnkName(invoice.getCompanyAgreement().getServiceProvider().getBankAccount().getBank().getName());
        inv.setFromBnkBic(invoice.getCompanyAgreement().getServiceProvider().getBankAccount().getBank().getBic());
        inv.setFromBnkAccNum(invoice.getCompanyAgreement().getServiceProvider().getBankAccount().getAccountNumber());
        inv.setToName(invoice.getCompanyAgreement().getServiceConsumer().getName());
        inv.setToRegNum(invoice.getCompanyAgreement().getServiceConsumer().getRegistrationNumber());
        inv.setToTaxNum(invoice.getCompanyAgreement().getServiceConsumer().getTaxPayerNumber());
        inv.setToAddr(invoice.getCompanyAgreement().getServiceConsumer().getAddress());
        inv.setToPhone(invoice.getCompanyAgreement().getServiceConsumer().getPhoneNumber());
        inv.setToBnkName(invoice.getCompanyAgreement().getServiceConsumer().getBankAccount().getBank().getName());
        inv.setToBnkBic(invoice.getCompanyAgreement().getServiceConsumer().getBankAccount().getBank().getBic());
        inv.setToBnkAccNum(invoice.getCompanyAgreement().getServiceConsumer().getBankAccount().getAccountNumber());
        inv.setAgrNum(invoice.getCompanyAgreement().getAgreementNumber());
        inv.setTtlBdat(convertAmount(invoice.getTotalAmountBeforeDiscountAndTax()));
        inv.setTtlDam(convertAmount(invoice.getTotalDiscountAmount()));
        inv.setTtlAbt(convertAmount(invoice.getTotalAmountBeforeTax()));
        inv.setTtlTa(convertAmount(invoice.getTotalTaxAmount()));
        inv.setTtlTbp(convertAmount(invoice.getTotalToBePaid()));
        inv.setTotalAmountWords(invoice.getTotalAmountWords());
        inv.setItems(invoice.getItems().stream().map(this::convert).collect(toList()));

        inv.setName(inv.getItems().iterator().next().getName());
        inv.setUnit(inv.getItems().iterator().next().getUnit());
        inv.setPrice(inv.getItems().iterator().next().getPrice());
        inv.setCurr(inv.getItems().iterator().next().getCurr());
        inv.setCount(inv.getItems().iterator().next().getCount());
        inv.setDp(inv.getItems().iterator().next().getDiscPerc());
        inv.setTxp(inv.getItems().iterator().next().getTaxPerc());
        inv.setSubTtl(inv.getItems().iterator().next().getSubTtl());
        return inv;
    }

    @Override
    public InvIt convert(InvoiceItem item) {
        InvIt invIt = new InvIt();
        invIt.setName(item.getName());
        invIt.setUnit(item.getUnit());
        invIt.setCount(convertCount(item.getCount()));
        invIt.setPrice(convertAmount(item.getPrice()));
        invIt.setDiscPerc(convertCount(item.getDiscountPercent()));
        invIt.setTaxPerc(convertCount(item.getTaxPercent()));
        invIt.setSubTtl(convertAmount(item.getSubTotal()));
        return invIt;
    }

}
