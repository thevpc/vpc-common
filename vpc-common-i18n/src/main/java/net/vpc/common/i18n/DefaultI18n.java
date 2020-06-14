/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.i18n;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.function.Function;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePList;
import net.vpc.common.props.WritablePValue;

/**
 *
 * @author vpc
 */
public class DefaultI18n implements I18n {

    private static final Function<String, String> DEFAULT_VALUE = (String t) -> "NotFound(" + t + ")";
    private WritablePValue<Function<String, String>> defaultValue = (WritablePValue) Props.of("bundles").valueOf(Function.class, DEFAULT_VALUE);
    private final Map<String, String> cache = new HashMap<>();
    private final Set<String> notFound = new HashSet<>();
    private final WritablePList<I18nBundle> bundles = Props.of("bundles").listOf(I18nBundle.class);

    public DefaultI18n() {
    }

    @Override
    public WritablePList<I18nBundle> bundles() {
        return bundles;
    }

    @Override
    public WritablePValue<Function<String, String>> defaultValue() {
        return defaultValue;
    }

    @Override
    public String getString(String name) {
        return getString(name, null);
    }

    @Override
    public String getString(String name, Function<String, String> defaultValue) {
        String a = cache.get(name);
        if (a != null) {
            return a;
        }
        if (notFound.contains(name)) {
            buildDefault(name, defaultValue);
        }
        for (I18nBundle bundle : bundles) {
            try {
                String v = bundle.getString(name);
                if (v != null) {
                    cache.put(name, v);
                    return v;
                }
            } catch (MissingResourceException e) {

            }
        }
        notFound.add(name);
        return buildDefault(name, defaultValue);
    }

    private String buildDefault(String name, Function<String, String> defaultValue) {
        if (defaultValue == null) {
            defaultValue = this.defaultValue.get();
        }
        if (defaultValue == null) {
            defaultValue = DEFAULT_VALUE;
        }
        return defaultValue.apply(name);
    }
}
