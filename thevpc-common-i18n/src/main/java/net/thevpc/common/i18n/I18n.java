/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import java.util.Locale;
import java.util.function.Function;
import net.thevpc.common.props.WritableValue;

/**
 *
 * @author thevpc
 */
public interface I18n {

    public WritableValue<Locale> locale();
    
    public I18nBundleList bundles();

    public String getString(String name);

    public String getString(String name, Function<String, String> defaultValue);

    WritableValue<Function<String, String>> defaultValue();

}
