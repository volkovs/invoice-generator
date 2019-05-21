package dev.volkovs.office.tool.invoice.generator.lv;

import dev.volkovs.office.tool.invoice.model.spi.LocaleProvider;

import java.util.Locale;

public class LvLocaleProvider implements LocaleProvider {

    private Locale locale = new Locale("lv", "LV");

    @Override
    public Locale get() {
        return locale;
    }
}
