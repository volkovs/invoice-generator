package dev.volkovs.office.tool.invoice.example;

import com.google.common.annotations.VisibleForTesting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.capitalize;

public class LvAmount2WordConverter {

    private static final Map<Long, String> digits = new HashMap<>();

    static {
        digits.put(0L, "");
        digits.put(1L, "viens");
        digits.put(2L, "divi");
        digits.put(3L, "trīs");
        digits.put(4L, "četri");
        digits.put(5L, "pieci");
        digits.put(6L, "seši");
        digits.put(7L, "septiņi");
        digits.put(8L, "astoņi");
        digits.put(9L, "deviņi");
        digits.put(10L, "desmit");
    }

    private static final Map<Long, String> prefixes = new HashMap<>();

    static {
        prefixes.put(1L, "vien");
        prefixes.put(2L, "div");
        prefixes.put(3L, "trīs");
        prefixes.put(4L, "četr");
        prefixes.put(5L, "piec");
        prefixes.put(6L, "seš");
        prefixes.put(7L, "septiņ");
        prefixes.put(8L, "astoņ");
        prefixes.put(9L, "deviņ");
    }

    @VisibleForTesting
    static String number2String(long amount) {
        if (amount == 0) {
            return "nulle";
        }
        return number2StringInternal(amount);
    }

    private static String number2StringInternal(long amount) {
        checkArgument(amount >= 0);

        if (amount <= 10) {
            return digits.get(amount);
        } else if (amount < 20) {
            return prefixes.get(amount % 10) + "padsmit";
        } else if (amount < 100) {
            return (prefixes.get(amount / 10) + "desmit " + number2StringInternal(amount % 10)).trim();
        } else if (amount < 200) {
            return ("viens simts " + number2StringInternal(amount % 100)).trim();
        } else if (amount < 1_000) {
            return (number2StringInternal(amount / 100) + " simti " + number2StringInternal(amount % 100)).trim();
        } else if (amount < 2_000) {
            return ("viens tukstots " + number2StringInternal(amount % 1000)).trim();
        } else if (amount < 1_000_000) {
            return (number2StringInternal(amount / 1_000) + " tukstoši " + number2StringInternal(amount % 1_000)).trim();
        } else if (amount < 2_000_000) {
            return ("viens miljons " + number2StringInternal(amount % 1_000_000)).trim();
        } else if (amount < 1_000_000_000) {
            return (number2StringInternal(amount / 1_000_000) + " miljoni " + number2StringInternal(amount % 1_000_000)).trim();
        } else if (amount < 2_000_000_000) {
            return ("viens miljards " + number2StringInternal(amount % 1_000_000_000)).trim();
        } else if (amount < 1_000_000_000_000L) {
            return (number2StringInternal(amount / 1_000_000_000) + " miljardi " + number2StringInternal(amount % 1_000_000_000)).trim();
        } else if (amount < 2_000_000_000_000L) {
            return ("viens triljons " + number2StringInternal(amount % 1_000_000_000_000L)).trim();
        } else if (amount < 1_000_000_000_000_000L) {
            return (number2StringInternal(amount / 1_000_000_000_000L) + " triljoni " + number2StringInternal(amount % 1_000_000_000_000L)).trim();
        }
        throw new RuntimeException("Too big number to name it - " + amount);
    }


    private long euros;

    private int cents;

    public LvAmount2WordConverter(BigDecimal amount) {
        checkArgument(amount.compareTo(BigDecimal.ZERO) >= 0);
        checkArgument(amount.compareTo(new BigDecimal("1000000000000000")) < 0);
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.euros = amount.toBigInteger().longValue();
        this.cents = amount.remainder(BigDecimal.ONE).multiply(new BigDecimal("100")).toBigInteger().intValue();
    }

    public String toWords() {
        return capitalize(format("%s %s", toEuros(), toCents()));
    }

    @VisibleForTesting
    String toEuros() {
        return number2String(euros) + " eiro";
    }

    @VisibleForTesting
    String toCents() {
        if (this.cents % 10 == 1 && this.cents != 11) {
            return this.cents + " cents";
        }
        return this.cents + " centi";
    }
}
