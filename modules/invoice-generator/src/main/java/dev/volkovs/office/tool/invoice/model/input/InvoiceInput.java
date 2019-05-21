package dev.volkovs.office.tool.invoice.model.input;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvoiceInput {
    private String companyToCharge;
    private List<InvoiceItemInput> items = new ArrayList<>();
}
