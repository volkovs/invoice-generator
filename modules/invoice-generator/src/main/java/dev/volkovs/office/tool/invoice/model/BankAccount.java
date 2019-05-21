package dev.volkovs.office.tool.invoice.model;

import lombok.Data;

@Data
public class BankAccount {
    private Bank bank = new Bank();
    private String accountNumber;
}
