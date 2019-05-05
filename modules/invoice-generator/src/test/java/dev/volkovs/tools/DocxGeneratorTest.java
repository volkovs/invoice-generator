package dev.volkovs.tools;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.itext.extension.font.ITextFontRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DocxGeneratorTest {

    private static final String TEMPLATE_NAME = "DocxProjectWithFreemarkerList";

    @Test
    public void xdocreport() throws IOException, XDocReportException {
        // 1) Load Docx file by filling Freemarker template engine and cache it to the registry
        InputStream in = DocxGeneratorTest.class.getClassLoader().getResourceAsStream(TEMPLATE_NAME + ".docx");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

        // 2) Create fields metadata to manage lazy loop ([#list Freemarker) for foot notes.
        FieldsMetadata metadata = report.createFieldsMetadata();
        metadata.load("developers", Developer.class, true);

        // 3) Create context Java model
        IContext context = report.createContext();
        Project project = new Project("XDocReport");
        context.put("project", project);
        // Register developers list
        List<Developer> developers = new ArrayList<>();
        developers.add(new Developer("Mani sauc Jānis", "Angelo", "angelo.zerr@gmail.com"));
        developers.add(new Developer("Leclercq", "Pascal", "pascal.leclercq@gmail.com"));
        context.put("developers", developers);

        // 4) Generate report by merging Java model with the Docx
        OutputStream out = new FileOutputStream(new File("target/" + TEMPLATE_NAME + "_generated.docx"));
        report.process(context, out);
    }

    @Test
    public void convertDocxToPdf() throws IOException {
        String fileOutName = "target/" + TEMPLATE_NAME + ".pdf";

        long startTime = System.currentTimeMillis();

        //Adjust the minInflateRatio otherwise the file triggers a false positive for a zip bomb
        ZipSecureFile.setMinInflateRatio(0);
        XWPFDocument document = new XWPFDocument(new FileInputStream("target/" + TEMPLATE_NAME + "_generated.docx"));

        OutputStream out = new FileOutputStream(new File(fileOutName));
        PdfOptions options = PdfOptions.create().fontEncoding("utf-8");

        options.fontProvider((familyName, encoding, size, style, color) -> {
            try {
                BaseFont bfChinese = BaseFont.createFont("src/main/resources/Arial Unicode.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font fontChinese = new Font(bfChinese, size, style, color);
                if (familyName != null) {
                    fontChinese.setFamily(familyName);
                }
                return fontChinese;
            } catch (Throwable e) {
                log.error("Error creating font", e);
                // An error occurs, use the default font provider.
                return ITextFontRegistry.getRegistry().getFont(familyName, encoding, size, style, color);
            }
        });

        PdfConverter.getInstance().convert(document, out, options);

        log.info("Generate " + fileOutName + " with " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    public static class Role {

        private final String name;

        public Role(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Developer {

        private final String name;
        private final String lastName;
        private final String mail;
        private final List<Role> roles;

        Developer(String name, String lastName, String mail) {
            this.name = name;
            this.lastName = lastName;
            this.mail = mail;
            this.roles = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public String getLastName() {
            return lastName;
        }

        public String getMail() {
            return mail;
        }

        public Developer addRole(Role role) {
            roles.add(role);
            return this;
        }

        public List<Role> getRoles() {
            return roles;
        }

    }

    public static class Project {

        private final String name;
        private String url;

        Project(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setURL(String url) {
            this.url = url;
        }

        public String getURL() {
            return url;
        }
    }

}
