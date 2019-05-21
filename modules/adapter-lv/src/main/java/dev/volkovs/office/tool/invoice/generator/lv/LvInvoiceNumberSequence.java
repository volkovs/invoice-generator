package dev.volkovs.office.tool.invoice.generator.lv;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import dev.volkovs.office.tool.invoice.model.spi.InvoiceNumberSequence;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class LvInvoiceNumberSequence implements InvoiceNumberSequence {

    private final String prefix = "TCK";

    private final Pattern invoiceNumberPattern;
    private final File sequenceFolder;

    public LvInvoiceNumberSequence() {
        this.invoiceNumberPattern = Pattern.compile(prefix + "-(\\d+)");
        String homeDirectory = System.getProperty("user.home");
        sequenceFolder = new File(homeDirectory, ".invoice-generator/invoice-number");
        if (!sequenceFolder.exists()) {
            sequenceFolder.mkdirs();
        }
    }

    @Override
    public String next() {
        try {
            int year = LocalDate.now().getYear();

            File container = new File(sequenceFolder, String.valueOf(year));
            if (!container.exists()) {
                container.mkdirs();
            }
            String[] invoiceNumbers = container.list((dir, name) -> invoiceNumberPattern.matcher(name).matches());

            int currentInvoiceNumber = currNumerical(invoiceNumbers);
            checkApproved(year, currentInvoiceNumber);

            int nextInvoiceNumber = currentInvoiceNumber + 1;
            String result = nextInvoiceNumber(year, nextInvoiceNumber);

            new File(container, fileNameFor(nextInvoiceNumber)).createNewFile();
            return result;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private String fileNameFor(int invoiceNumber) {
        return prefix + "-" + invoiceNumber;
    }

    private void checkApproved(int year, int currentInvoiceNumber) {
        String fileName = fileNameFor(currentInvoiceNumber);
        File currentInvoiceNumberFile = new File(this.sequenceFolder, year + "/" + fileName);
        if (currentInvoiceNumberFile.length() == 0) {
            String message = "Previous invoice number %s was not approved.\n" +
                    "To review  please type:\nopen %s\n" +
                    "To approve please type:\necho approved > %s";
            throw new RuntimeException(format(message, fileName, currentInvoiceNumberFile.getParentFile().getAbsolutePath(), currentInvoiceNumberFile.getAbsolutePath()));
        }
    }

    @VisibleForTesting
    int currNumerical(String... invoiceNumbers) {
        return Arrays.stream(invoiceNumbers).map(fileName -> {
            Matcher matcher = invoiceNumberPattern.matcher(fileName);
            if (matcher.matches()) {
                return Integer.valueOf(matcher.group(1));
            }
            return -1;
        }).max(Comparator.naturalOrder()).orElse(0);
    }

    @VisibleForTesting
    String nextInvoiceNumber(int year, int nextInvoiceNumber) {
        return format("%s-%s/%s", prefix, nextInvoiceNumber, year);
    }

}
