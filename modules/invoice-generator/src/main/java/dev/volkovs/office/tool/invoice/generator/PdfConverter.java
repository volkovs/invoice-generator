package dev.volkovs.office.tool.invoice.generator;

import com.google.common.base.Throwables;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.itext.extension.font.ITextFontRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static fr.opensagres.poi.xwpf.converter.pdf.PdfConverter.getInstance;

@Slf4j
public class PdfConverter {

    public static void convertTo(File docxSource, File pdfDestination) {
        try {
            convertTo(new FileInputStream(docxSource), new FileOutputStream(pdfDestination));
        } catch (FileNotFoundException e) {
            Throwables.propagate(e);
        }
    }

    private static void convertTo(InputStream docxSource, OutputStream pdfDestination) {

        long startTime = System.currentTimeMillis();

        //Adjust the minInflateRatio otherwise the file triggers a false positive for a zip bomb
        ZipSecureFile.setMinInflateRatio(0);
        try {
            XWPFDocument document = new XWPFDocument(docxSource);
            PdfOptions options = PdfOptions.create().fontEncoding("utf-8");
            options.fontProvider((familyName, encoding, size, style, color) -> {
                String fontLocation = "src/main/resources/Arial Unicode.ttf";
                try {
                    BaseFont bfChinese = BaseFont.createFont(fontLocation, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font fontChinese = new Font(bfChinese, size, style, color);
                    if (familyName != null) {
                        fontChinese.setFamily(familyName);
                    }
                    return fontChinese;
                } catch (Exception e) {
                    log.error("Error creating font " + fontLocation, e);
                    // An error occurs, use the default font provider.
                    return ITextFontRegistry.getRegistry().getFont(familyName, encoding, size, style, color);
                }
            });

            getInstance().convert(document, pdfDestination, options);
        } catch (IOException e) {
            log.error("Error converting to PDF", e);
        }

        log.info("Generated in " + (System.currentTimeMillis() - startTime) + " millis.");
    }

}
