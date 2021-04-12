/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableValue;

/**
 *
 * @author thevpc
 */
public class DefaultI18n implements I18n {

    private static Logger LOG = Logger.getLogger(DefaultI18n.class.getName());

    private static final Function<String, String> DEFAULT_VALUE = (String t) -> {
        LOG.log(Level.FINEST, "I18n String not found: {0}", t);
        return "NotFound(" + t + ")";
    };
    private WritableValue<Function<String, String>> defaultValue = (WritableValue) Props.of("bundles").valueOf(Function.class, DEFAULT_VALUE);
    private final Map<String, String> cache = new HashMap<>();
    private final Set<String> notFound = new HashSet<>();
    private final I18nBundleList bundles = new DefaultI18nBundleList("bundles");
    private final WritableValue<Locale> locale = Props.of("locale").valueOf(Locale.class, Locale.getDefault());
    private int maxReports = 2000;
    private String id;

    public DefaultI18n(String id) {
        this.id=id;
        locale.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                cache.clear();
            }
        });
    }

    public String getId() {
        return id;
    }
    

    @Override
    public WritableValue<Locale> locale() {
        return locale;
    }

    @Override
    public I18nBundleList bundles() {
        return bundles;
    }

    @Override
    public WritableValue<Function<String, String>> defaultValue() {
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
            return buildDefault(name, defaultValue);
        }
        for (I18nBundle bundle : bundles) {
            try {
                Locale locale2 = locale.get();
                String v = bundle.getString(name, locale2 == null ? Locale.getDefault() : locale2);
                if (v != null) {
                    cache.put(name, v);
                    return v;
                }
            } catch (MissingResourceException e) {

            }
        }
        LOG.log(Level.FINER, "i18n {0}: string not found: {1}", new Object[]{getId(), name});
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
