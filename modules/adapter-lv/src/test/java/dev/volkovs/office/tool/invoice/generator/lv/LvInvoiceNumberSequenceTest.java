package dev.volkovs.office.tool.invoice.generator.lv;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LvInvoiceNumberSequenceTest {

    private LvInvoiceNumberSequence sequence;

    @BeforeEach
    void setUp() {
        sequence = new LvInvoiceNumberSequence();
    }

    @Test
    void nextInvoiceNumber() {
        String nextInvoiceNumber = sequence.nextInvoiceNumber(2019, 15);
        assertThat(nextInvoiceNumber).isEqualTo("TCK-15/2019");
    }

    @Test
    void currNumerical() {
        int current = sequence.currNumerical("TCK-2", "TCK-3", "TCK-1");
        assertThat(current).isEqualTo(3);
    }

    @Test
    @Disabled("Tested")
    void next() {
        log.info(sequence.next());
    }

}