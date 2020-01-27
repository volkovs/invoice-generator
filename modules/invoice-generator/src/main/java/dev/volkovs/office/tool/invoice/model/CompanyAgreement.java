package dev.volkovs.office.tool.invoice.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyAgreement {
    private Company serviceProvider;
    private Company serviceConsumer;
    private String agreementNumber;
    private LocalDate agreementDate;
}
