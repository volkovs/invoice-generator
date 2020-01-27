package dev.volkovs.office.tool.invoice.model;

import lombok.Data;

@Data
public class Company {
    private String name;
    private String registrationNumber;
    private String taxPayerNumber;
    private String address;
    private String phoneNumber;
    private BankAccount bankAccount = new BankAccount();
    private String ceo;
}
