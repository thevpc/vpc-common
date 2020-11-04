/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */

package net.thevpc.common.prs.messageset;

import java.util.*;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class MessageSetResourceBundleWrapper extends AbstractMessageSetBundle {
    private ResourceBundle bundle;
    private String preferredName;
    private ClassLoader classLoader;

    public MessageSetResourceBundleWrapper(String baseName, Locale locale, ClassLoader classLoader) {
        this.classLoader = classLoader;
        preferredName = baseName;
        setLocale(locale);
        revalidate();
    }

    @Override
    public void revalidate() {
        super.revalidate();
        this.bundle = Boolean.getBoolean(MessageSetResourceBundleWrapper.class.getName()+".defaultImpl")?
                getDefaultBundle(preferredName, getLocale(), classLoader)
                : getVariantBundle(preferredName, getLocale(), classLoader)
                ;
    }

    /**
     * this is the default impls but it seems not to work in JDK 6
     * @param baseName baseName
     * @param locale locale
     * @param loader class loader
     * @return ResourceBundle
     */
    private ResourceBundle getDefaultBundle(String baseName, Locale locale,
                                     ClassLoader loader) {
        return loader == null ?
                ResourceBundle.getBundle(baseName, locale)
                : ResourceBundle.getBundle(baseName, locale, loader)
                ;
    }

    /**
     * Bug fixing impl for JDK>=6
     * @param baseName baseName
     * @param locale locale
     * @param loader class loader
     * @return ResourceBundle
     */
    private ResourceBundle getVariantBundle(String baseName, Locale locale,
                                     ClassLoader loader) {
        String[] n = getResourceNames(baseName, locale);
        Properties p = new Properties();
        if(loader==null){
            loader=getClass().getClassLoader();
        }
        for (int i=n.length-1;i>=0;i--) {
            String s=n[i];
            Properties pi = new Properties();
            InputStream is = null;
            try {
                URL url = loader.getResource(s);
                if (url != null) {
                    is = url.openStream();
                    pi.load(is);
                }
            } catch (IOException e) {
                //
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
            if(pi.size()>0){
                if(Boolean.getBoolean("MessageSetResourceBundleWrapper.dontInherits")){
                    p.clear();
                }
                p.putAll(pi);
            }
        }
        return new MapResourceBundle(p);
    }

    private String[] getResourceNames(String baseName, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        baseName = baseName.replace('.', '/');
        String l = locale.getLanguage();
        String c = locale.getCountry();
        String v = locale.getVariant();
        ArrayList<String> prefixes = new ArrayList<String>();
        if (v.length() != 0) {
            prefixes.add(baseName + "_" + l + "_" + c + "_" + l + ".properties");
        }
        if (c.length() != 0) {
            prefixes.add(baseName + "_" + l + "_" + c + ".properties");
        }
        if (l.length() != 0) {
            prefixes.add(baseName + "_" + l + ".properties");
        }
        prefixes.add(baseName + ".properties");
        return prefixes.toArray(new String[prefixes.size()]);
    }

    public MessageSetResourceBundleWrapper(String baseName, Locale locale) {
        this(baseName, locale, null);
    }

    public String getString/*NoCache*/(String key) throws MissingResourceException {
        return bundle.getString(key);
    }

    @Override
    public String toString() {
        return "ResourceBundleWrapper(" + preferredName + ")";
    }

}
