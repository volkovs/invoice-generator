package dev.volkovs.office.tool.invoice.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.volkovs.office.tool.invoice.model.Bank;
import dev.volkovs.office.tool.invoice.model.BankAccount;
import dev.volkovs.office.tool.invoice.model.Company;
import dev.volkovs.office.tool.invoice.model.input.Settings;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static dev.volkovs.office.tool.invoice.example.Application.getApplicationHome;

class ExampleSettingsGenerator {

    @Test
    void generateSettings() throws IOException {
        Settings settings = exampleSettings();
        File settingsFile = new File(getApplicationHome(), "settings.json");
        if (settingsFile.exists()) {
            throw new RuntimeException(String.format("Settings file already exists (%s)", settingsFile.getAbsolutePath()));
        }
        settingsFile.createNewFile();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new FileOutputStream(settingsFile, false), settings);
    }

    private Settings exampleSettings() {
        Bank bank = new Bank();
        bank.setBic("HABALV22");
        bank.setName("Swedbank AS");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("LV39HABA0551231231234");
        bankAccount.setBank(bank);

        Company sender = new Company();
        sender.setName("Provider SIA");
        sender.setRegistrationNumber("40103101234");
        sender.setTaxPayerNumber("LV40103101234");
        sender.setAddress("Provajderu 3B, Rīga LV-1050");
        sender.setPhoneNumber("+371 29123456");
        sender.setBankAccount(bankAccount);

        Company partner1 = new Company();
        partner1.setName("Tails Consulting SIA");
        partner1.setRegistrationNumber("40203101234");
        partner1.setTaxPayerNumber("LV40203101234");
        partner1.setAddress("Astu prospekts 486, LV-2050");

        Company partner2 = new Company();
        partner2.setName("Airworld SIA");
        partner2.setRegistrationNumber("40303101234");
        partner2.setTaxPayerNumber("LV40303101234");
        partner2.setAddress("Marupe, LV-2050");

        Map<String, Company> agreements = new HashMap<>();
        agreements.put("2018-01-01", partner1);
        agreements.put("2018-01-02", partner2);

        Settings settings = new Settings();
        settings.setSender(sender);
        settings.setAgreements(agreements);
        settings.setVatPercent(new BigDecimal("21"));
        settings.setCurrency("EUR");
        return settings;
    }

}
