/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;


import java.util.Locale;
import java.util.function.Function;

/**
 * @author thevpc
 */
public interface I18nLocale {
    Locale currentLocale();

    String getString(String name);

    String getString(String name, Function<String, String> defaultValue);
}
