package dev.volkovs.office.tool.invoice.generator.lv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class LvModelConverterTest {

    private LvModelConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LvModelConverter();
    }

    @Test
    void convertAmount() {
        assertThat(converter.convertAmount(new BigDecimal("0"))).isEqualTo("0.00");
        assertThat(converter.convertAmount(new BigDecimal("9"))).isEqualTo("9.00");
        assertThat(converter.convertAmount(new BigDecimal("9.9"))).isEqualTo("9.90");
        assertThat(converter.convertAmount(new BigDecimal("9.99"))).isEqualTo("9.99");
        assertThat(converter.convertAmount(new BigDecimal("9.991"))).isEqualTo("9.99");
        assertThat(converter.convertAmount(new BigDecimal("9.999"))).isEqualTo("10.00");
        assertThat(converter.convertAmount(new BigDecimal("1000"))).isEqualTo("1 000.00");
        assertThat(converter.convertAmount(new BigDecimal("10000"))).isEqualTo("10 000.00");
    }

    @Test
    void convertCount() {
        assertThat(converter.convertCount(new BigDecimal("0"))).isEqualTo("0");
        assertThat(converter.convertCount(new BigDecimal("1"))).isEqualTo("1");
        assertThat(converter.convertCount(new BigDecimal("0.5"))).isEqualTo("0.5");
        assertThat(converter.convertCount(new BigDecimal("0.500"))).isEqualTo("0.5");
        assertThat(converter.convertCount(new BigDecimal("21"))).isEqualTo("21");
    }

    @Test
    void convert() {
        LocalDate date = LocalDate.of(2019, 5, 18);
        assertThat(converter.convert(date)).isEqualTo("2019. gada 18. maijs");
    }
}