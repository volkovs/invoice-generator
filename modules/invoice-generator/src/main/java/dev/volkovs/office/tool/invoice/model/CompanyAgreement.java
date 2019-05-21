package dev.volkovs.office.tool.invoice.model;

import lombok.Data;

@Data
public class CompanyAgreement {
    private Company serviceProvider;
    private Company serviceConsumer;
    private String agreementNumber;
}
