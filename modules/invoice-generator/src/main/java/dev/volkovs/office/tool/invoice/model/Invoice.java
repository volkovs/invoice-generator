package dev.volkovs.office.tool.invoice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class Invoice {

    private CompanyAgreement companyAgreement;

    private LocalDate date;
    private LocalDate due;
    private LocalDate workPeriodFirstDay;
    private LocalDate workPeriodLastDay;

    private String number;

    private List<InvoiceItem> items;

    private BigDecimal totalAmountBeforeDiscountAndTax;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalAmountBeforeTax;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalToBePaid;

    private String totalAmountWords;

}
