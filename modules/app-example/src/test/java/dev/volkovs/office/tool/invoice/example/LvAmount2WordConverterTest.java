package dev.volkovs.office.tool.invoice.example;

import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class LvAmount2WordConverterTest {

    @Test
    void toCents() {
        assertCents("0", "0 centi");
        assertCents("1", "0 centi");
        assertCents("1.0", "0 centi");
        assertCents("1.00", "0 centi");
        assertCents("1.000", "0 centi");

        assertCents("0.1", "10 centi");
        assertCents("0.10", "10 centi");
        assertCents("0.100", "10 centi");

        assertCents("0.01", "1 cents");
        assertCents("0.02", "2 centi");
        assertCents("0.11", "11 centi");
        assertCents("0.21", "21 cents");
        assertCents("0.31", "31 cents");
        assertCents("0.41", "41 cents");
        assertCents("0.51", "51 cents");
        assertCents("0.61", "61 cents");
        assertCents("0.71", "71 cents");
        assertCents("0.81", "81 cents");
        assertCents("0.91", "91 cents");
    }

    @Test
    void number2String() {
        assertK2String(0, "nulle");
        assertK2String(1, "viens");
        assertK2String(2, "divi");
        assertK2String(3, "trīs");
        assertK2String(4, "četri");
        assertK2String(5, "pieci");
        assertK2String(6, "seši");
        assertK2String(7, "septiņi");
        assertK2String(8, "astoņi");
        assertK2String(9, "deviņi");
        assertK2String(10, "desmit");
        assertK2String(11, "vienpadsmit");
        assertK2String(12, "divpadsmit");
        assertK2String(13, "trīspadsmit");
        assertK2String(14, "četrpadsmit");
        assertK2String(15, "piecpadsmit");
        assertK2String(16, "sešpadsmit");
        assertK2String(17, "septiņpadsmit");
        assertK2String(18, "astoņpadsmit");
        assertK2String(19, "deviņpadsmit");
        assertK2String(20, "divdesmit");
        assertK2String(30, "trīsdesmit");
        assertK2String(40, "četrdesmit");
        assertK2String(50, "piecdesmit");
        assertK2String(60, "sešdesmit");
        assertK2String(70, "septiņdesmit");
        assertK2String(80, "astoņdesmit");
        assertK2String(90, "deviņdesmit");
        assertK2String(100, "viens simts");
        assertK2String(200, "divi simti");

        assertK2String(1000, "viens tukstots");
        assertK2String(2000, "divi tukstoši");
        assertK2String(20000, "divdesmit tukstoši");
        assertK2String(200000, "divi simti tukstoši");
        assertK2String(1000000, "viens miljons");
        assertK2String(2000000, "divi miljoni");
        assertK2String(20000000, "divdesmit miljoni");
        assertK2String(200000000, "divi simti miljoni");
        assertK2String(1000000000, "viens miljards");
        assertK2String(2000000000, "divi miljardi");
        assertK2String(1000000000000L, "viens triljons");
        assertK2String(2000000000000L, "divi triljoni");

        assertK2String(2691, "divi tukstoši seši simti deviņdesmit viens");

        assertK2String(2123123123123L, "divi triljoni viens simts divdesmit trīs miljardi viens simts divdesmit trīs miljoni viens simts divdesmit trīs tukstoši viens simts divdesmit trīs");

    }

    @Test
    void toEuros() {
        assertEuros("0", "nulle eiro");

        assertEuros("1000", "viens tukstots eiro");
        assertEuros("2000", "divi tukstoši eiro");
        assertEuros("20000", "divdesmit tukstoši eiro");
        assertEuros("200000", "divi simti tukstoši eiro");
        assertEuros("2000000", "divi miljoni eiro");
        assertEuros("20000000", "divdesmit miljoni eiro");
        assertEuros("200000000", "divi simti miljoni eiro");
        assertEuros("2000000000", "divi miljardi eiro");
        assertEuros("2000000000000", "divi triljoni eiro");

        assertEuros("2691", "divi tukstoši seši simti deviņdesmit viens eiro");

        assertEuros("2123123123123", "divi triljoni viens simts divdesmit trīs miljardi viens simts divdesmit trīs miljoni viens simts divdesmit trīs tukstoši viens simts divdesmit trīs eiro");
    }

    @Test
    void toWords() {
        assertWords("0", "Nulle eiro 0 centi");
        assertWords("2690.22", "Divi tukstoši seši simti deviņdesmit eiro 22 centi");
    }

    @Test
    void toWordsNegativaAmount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new LvAmount2WordConverter(new BigDecimal("-1")));
    }

    private AbstractStringAssert<?> assertWords(String amount, String expectedWords) {
        return assertThat(new LvAmount2WordConverter(new BigDecimal(amount)).toWords()).isEqualTo(expectedWords);
    }

    private AbstractStringAssert<?> assertCents(String amount, String expectedWords) {
        return assertThat(new LvAmount2WordConverter(new BigDecimal(amount)).toCents()).isEqualTo(expectedWords);
    }

    private AbstractStringAssert<?> assertEuros(String amount, String expectedWords) {
        return assertThat(new LvAmount2WordConverter(new BigDecimal(amount)).toEuros()).isEqualTo(expectedWords);
    }

    private AbstractStringAssert<?> assertK2String(long amount, String expectedWords) {
        return assertThat(LvAmount2WordConverter.number2String(amount)).isEqualTo(expectedWords);
    }
}