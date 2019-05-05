package dev.volkovs.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface DocumentGenerator {

    void generate(InputStream template, Map<String, Object> beans, OutputStream document);

}
