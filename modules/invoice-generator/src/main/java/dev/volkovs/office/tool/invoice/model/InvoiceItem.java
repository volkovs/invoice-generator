package dev.volkovs.office.tool.invoice.model;

import dev.volkovs.office.tool.invoice.model.input.InvoiceItemInput;
import dev.volkovs.office.tool.invoice.model.input.Settings;
import lombok.Data;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

@Data
public class InvoiceItem {
    private String name;
    private String unit;
    private BigDecimal count;
    private BigDecimal price;
    private String currency;
    private BigDecimal discountPercent;
    private BigDecimal taxPercent;
    private BigDecimal subTotal;

    private BigDecimal totalDiscountAmount;
    private BigDecimal totalAmountBeforeTax;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalToBePaid;

    public static InvoiceItem valueOf(InvoiceItemInput input, Settings settings) {
        BigDecimal subTotal = input.getPrice().multiply(input.getCount());
        BigDecimal discountPercent = input.getDiscountPercent();
        BigDecimal taxPercent = settings.getVatPercent();

        InvoiceItem item = new InvoiceItem();
        item.name = input.getName();
        item.unit = input.getUnit();
        item.count = input.getCount();
        item.price = input.getPrice();
        item.currency = settings.getCurrency();
        item.discountPercent = discountPercent;
        item.taxPercent = taxPercent;
        item.subTotal = subTotal;
        item.totalDiscountAmount = subTotal.multiply(discountPercent).divide(new BigDecimal("100"), 2, HALF_UP);
        item.totalAmountBeforeTax = subTotal.subtract(item.totalDiscountAmount);
        item.totalTaxAmount = item.totalAmountBeforeTax.multiply(taxPercent).divide(new BigDecimal("100"), 2, HALF_UP);
        item.totalToBePaid = item.totalAmountBeforeTax.add(item.totalTaxAmount);
        return item;
    }
}
