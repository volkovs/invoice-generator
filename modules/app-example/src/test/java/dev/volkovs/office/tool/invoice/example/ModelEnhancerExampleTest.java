package dev.volkovs.office.tool.invoice.example;

import dev.volkovs.office.tool.invoice.model.Invoice;
import dev.volkovs.office.tool.invoice.model.InvoiceItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

class ModelEnhancerExampleTest {

    private ModelEnhancerExample enhancer;
    private Invoice model;

    @BeforeEach
    void setUp() {
        enhancer = new ModelEnhancerExample();
        model = new Invoice();
        model.setDate(LocalDate.now());
        model.setItems(newArrayList(new InvoiceItem()));
    }

    @Test
    void enhance() {
        enhancer.enhance(model);
        assertThat(model.getItems().iterator().next().getName()).isNotEmpty();
    }

    @Test
    void enhanceAborted_tooManyItems() {
        model.setItems(newArrayList(new InvoiceItem(), new InvoiceItem()));
        enhancer.enhance(model);
        assertThat(model.getItems().iterator().next().getName()).isNull();
    }

    @Test
    void enhanceAborted_nameAlreadyDefined() {
        InvoiceItem item = model.getItems().iterator().next();
        item.setName("Something");
        enhancer.enhance(model);
        assertThat(item.getName()).isEqualTo("Something");
    }

    @Test
    void generateItemName() {
        LocalDate invoicingDate = LocalDate.of(2019, 4, 14);
        assertThat(enhancer.generateItemName(invoicingDate)).isEqualTo("IT pakalpojumi (2019. g. marts)");
    }
}