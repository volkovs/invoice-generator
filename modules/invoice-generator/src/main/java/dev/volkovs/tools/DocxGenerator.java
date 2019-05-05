package dev.volkovs.tools;

import com.google.common.base.Throwables;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class DocxGenerator implements DocumentGenerator {

    public void generate(File docxTemplate, Map<String, Object> beans, File docxDocument) {
        try {
            generate(new FileInputStream(docxTemplate), beans, new FileOutputStream(docxDocument));
        } catch (FileNotFoundException e) {
            Throwables.propagate(e);
        }
    }

    public void generate(InputStream docxTemplate, Map<String, Object> beans, OutputStream docxDocument) {
        try {

            // 1) Load Docx file by filling Freemarker template engine and cache it to the registry
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(docxTemplate, TemplateEngineKind.Freemarker);

            // 2) Create fields metadata to manage lazy loop ([#list Freemarker) for foot notes.
//            FieldsMetadata metadata = report.createFieldsMetadata();
//            metadata.load("developers", Developer.class, true);

            // 3) Create context Java model
            IContext context = report.createContext();
            beans.forEach(context::put);

            // 4) Generate report by merging Java model with the Docx
            report.process(context, docxDocument);

        } catch (IOException | XDocReportException e) {
            Throwables.propagate(e);
        }
    }

}
