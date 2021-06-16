/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.impl.PropertyBase;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thevpc
 */
public class DefaultI18n extends PropertyBase implements I18n {

    private static Logger LOG = Logger.getLogger(DefaultI18n.class.getName());

    private static final Function<String, String> DEFAULT_VALUE = (String t) -> {
        LOG.log(Level.FINEST, "I18n String not found: {0}", t);
        return "NotFound(" + t + ")";
    };
    private final I18nBundleList bundles = new DefaultI18nBundleList("bundles");
    private final WritableValue<Locale> locale = Props.of("locale").valueOf(Locale.class, Locale.getDefault());
    private final WritableList<Locale> availableLocales = Props.of("availableLocales").listOf(Locale.class);
    private final Map<Locale, I18nLocale> loc0 = new HashMap<>();
    private WritableValue<Function<String, String>> defaultValue = (WritableValue) Props.of("defaultValue").valueOf(Function.class, DEFAULT_VALUE);
    private int maxReports = 2000;

    public DefaultI18n(String id) {
        super(id);
//        locale.onChange(()->{
//            System.out.println("locale changed "+locale.get());
//
//        });
        propagateEvents(bundles);
        propagateEvents(locale);
        propagateEvents(defaultValue);
    }

    public WritableValue<Locale> locale() {
        return locale;
    }

    public WritableList<Locale> locales() {
        return availableLocales;
    }

    @Override
    public I18nBundleList bundles() {
        return bundles;
    }

    @Override
    public Locale currentLocale() {
        return current().currentLocale();
    }

    @Override
    public I18nLocale current() {
        Locale loc = locale().get();
        if (loc == null) {
            loc = Locale.getDefault();
        }
        return locale(loc);
    }

    @Override
    public I18nLocale locale(Locale locale) {
        if (locale == null) {
            locale = locale().get();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return loc0.computeIfAbsent(locale, new Function<Locale, I18nLocale>() {
            @Override
            public I18nLocale apply(Locale locale) {
                return new DefaultI18nLocale(DefaultI18n.this, locale);
            }
        });
    }

    @Override
    public String getString(String name) {
        return current().getString(name);
    }

    @Override
    public String getString(String name, Function<String, String> defaultValue) {
        return current().getString(name, defaultValue);
    }

    @Override
    public WritableValue<Function<String, String>> defaultValue() {
        return defaultValue;
    }

    @Override
    public I18n i18n() {
        return this;
    }

    private static class DefaultI18nLocale implements I18nLocale {
        private final Map<String, String> cache = new HashMap<>();
        private final Set<String> notFound = new HashSet<>();
        private DefaultI18n man;
        private Locale loc;

        public DefaultI18nLocale(DefaultI18n man, Locale loc) {
            this.man = man;
            this.loc = loc;
        }

        @Override
        public Locale currentLocale() {
            return loc;
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
            int size = this.man.bundles.size();
            for (int i = size - 1; i >= 0; i--) {
                I18nBundle bundle = this.man.bundles.get(i);
                try {
                    String v = bundle.getString(name, loc);
                    if (v != null) {
                        cache.put(name, v);
                        return v;
                    }
                } catch (MissingResourceException e) {

                }
            }
            //LOG.log(Level.FINER, "i18n {0}: string not found: {1}", new Object[]{this.man.propertyName(), name});
            notFound.add(name);
            return buildDefault(name, defaultValue);
        }

        private String buildDefault(String name, Function<String, String> defaultValue) {
            if (defaultValue == null) {
                defaultValue = this.man.defaultValue.get();
            }
            if (defaultValue == null) {
                defaultValue = DEFAULT_VALUE;
            }
            return defaultValue.apply(name);
        }

    }

}
