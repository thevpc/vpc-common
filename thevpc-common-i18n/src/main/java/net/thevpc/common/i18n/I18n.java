/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import net.thevpc.common.props.Property;
import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.WritableValue;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author thevpc
 */
public interface I18n extends Property, I18nLocale {

    WritableValue<Locale> locale();

    WritableList<Locale> locales();

    I18nBundleList bundles();

    I18n i18n();

    I18nLocale current();

    I18nLocale locale(Locale locale);

    @Override
    String getString(String name);

    @Override
    String getString(String name, Function<String, String> defaultValue);

    WritableValue<Function<String, String>> defaultValue();

}
