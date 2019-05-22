package dev.volkovs.office.tool.invoice.model.input;

import dev.volkovs.office.tool.invoice.model.Company;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Data
@ToString
public class Settings {

    private Company sender;
    private Map<String, Company> agreements;
    private BigDecimal vatPercent;
    private String currency;
    private String targetFolder;

    private String invoiceTemplateName = "templates/standard-invoice.docx";

    public Optional<String> findAgreementNumberByCompanyName(String companyToCharge) {
        AtomicReference<String> agreementFound = new AtomicReference<>();
        agreements.forEach((agreement, company) -> {
            String fullCompanyName = company.getName().toUpperCase();
            if (fullCompanyName.contains(companyToCharge.toUpperCase())) {
                agreementFound.set(agreement);
            }
        });
        return Optional.ofNullable(agreementFound.get());
    }

}
