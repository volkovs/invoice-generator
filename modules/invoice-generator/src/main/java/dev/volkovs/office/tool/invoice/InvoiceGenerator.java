package dev.volkovs.office.tool.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.volkovs.office.tool.invoice.generator.DocxGenerator;
import dev.volkovs.office.tool.invoice.generator.PdfConverter;
import dev.volkovs.office.tool.invoice.model.Invoice;
import dev.volkovs.office.tool.invoice.model.flat.Inv;
import dev.volkovs.office.tool.invoice.model.input.InvoiceInput;
import dev.volkovs.office.tool.invoice.model.input.Settings;
import dev.volkovs.office.tool.invoice.model.spi.ModelConverter;
import dev.volkovs.office.tool.invoice.model.spi.ModelEnhancer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Slf4j
public class InvoiceGenerator {

    private ObjectMapper mapper = new ObjectMapper();

    public void generate(InvoiceInput input, Settings settings) throws FileNotFoundException {
        Invoice invoice = new ModelGenerator(input, settings).generate();
        ServiceLoader.load(ModelEnhancer.class).forEach(enhancer -> enhancer.enhance(invoice));

        ModelConverter modelConverter = ServiceLoader.load(ModelConverter.class).iterator().next();
        Inv inv = modelConverter.convert(invoice);

        @SuppressWarnings("unchecked")
        Map<String, Object> beans = mapper.convertValue(inv, Map.class);

        String templateName = settings.getInvoiceTemplateName();
        InputStream template = getClass().getClassLoader().getResourceAsStream(templateName);
        File documentName = new File(settings.getTargetFolder(), new InvoiceFileNameGenerator().generate(invoice));
        if (documentName.exists()) {
            throw new RuntimeException(format("Document already exists (%s)", documentName.getAbsolutePath()));
        }
        FileOutputStream document = new FileOutputStream(documentName);

        new DocxGenerator().generate(template, beans, document);

        log.info("Invoice successfully generated:");
        log.info(format("open %s", documentName.getAbsolutePath()));

//        log.info("Converting to PDF");
//        convertToPdf(documentName);

        log.info("To approve type: ");

        // e.g. XXX-11/2019
        String invoiceNumberString = invoice.getNumber();
        Matcher matcher = Pattern.compile("(.+)/(.+)").matcher(invoiceNumberString);
        if (matcher.matches()) {
            String invoiceNumber = matcher.group(1);
            String invoiceYear = matcher.group(2);
            log.info(format("echo approved > /Users/mihails.volkovs/.invoice-generator/invoice-number/%s/%s", invoiceYear, invoiceNumber));
        }
    }

    void convertToPdf(File docx) {
        File pdf = new File(docx.getAbsolutePath().replace(".docx", ".pdf"));
        if (pdf.exists()) {
            throw new RuntimeException(format("Document already exists (%s)", pdf.getAbsolutePath()));
        }

        PdfConverter.convertTo(docx, pdf);
        log.info(format("PDF generated successfully: %s", pdf.getAbsolutePath()));
    }

}
