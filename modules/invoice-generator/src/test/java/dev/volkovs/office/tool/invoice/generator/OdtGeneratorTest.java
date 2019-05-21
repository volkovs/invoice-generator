package dev.volkovs.office.tool.invoice.generator;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
class OdtGeneratorTest {

    @Test
    void generate() throws FileNotFoundException {
        TestModel model = new TestModel("Opa");

        // building model
        Map<String, Object> beans = Maps.newHashMap();
        beans.put("name", "Technomen");
        beans.put("myBean", model);

        // processing template
        String templateName = "sample-template";
        InputStream template = this.getClass().getClassLoader().getResourceAsStream(templateName + ".odt");
        checkNotNull(template);
        FileOutputStream generated = new FileOutputStream("target/" + templateName + "_generated.odt");
        new OdtDocumentGenerator().generate(template, beans, generated);
        log.info("File {} completed", templateName);
    }

    @Data
    public static class TestModel {
        private final String name;
    }

}
