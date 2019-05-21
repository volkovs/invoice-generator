package dev.volkovs.office.tool.invoice.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.volkovs.office.tool.invoice.InvoiceGenerator;
import dev.volkovs.office.tool.invoice.model.input.InvoiceInput;
import dev.volkovs.office.tool.invoice.model.input.InvoiceItemInput;
import dev.volkovs.office.tool.invoice.model.input.Settings;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import static java.math.BigDecimal.ZERO;

@Slf4j
public class Application {

    public static void main(String... args) throws IOException {
        Settings settings = getSettings();
        log.info(settings.toString());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type in company to charge:");
        String companyToCharge = scanner.nextLine();
        System.out.println("Please type in amount to charge:");
        BigDecimal amounToCharge = new BigDecimal(scanner.nextLine());
        new InvoiceGenerator().generate(getInput(companyToCharge, amounToCharge), settings);
    }

    private static InvoiceInput getInput(String companyToCharge, BigDecimal amounToCharge) {
        InvoiceItemInput itemInput = new InvoiceItemInput();
        itemInput.setUnit("gb.");
        itemInput.setCount(new BigDecimal("1"));
        itemInput.setPrice(amounToCharge);
        itemInput.setDiscountPercent(ZERO);

        // name to be enhanced via SPI
        itemInput.setName(null);

        InvoiceInput input = new InvoiceInput();
        input.setCompanyToCharge(companyToCharge);
        input.getItems().add(itemInput);
        return input;
    }

    private static Settings getSettings() throws IOException {
        File settingsFile = new File(getApplicationHome(), "settings.json");
        if (!settingsFile.exists()) {
            throw new RuntimeException(String.format("Settings file does not exist (%s)", settingsFile.getAbsolutePath()));
        }
        return new ObjectMapper().readValue(new FileInputStream(settingsFile), Settings.class);
    }

    static File getApplicationHome() {
        String homeDirectory = System.getProperty("user.home");
        return new File(homeDirectory, ".invoice-generator");
    }

}
