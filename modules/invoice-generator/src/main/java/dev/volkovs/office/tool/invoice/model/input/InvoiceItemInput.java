package dev.volkovs.office.tool.invoice.model.input;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemInput {
    private String name;
    private String unit;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal discountPercent;
}
