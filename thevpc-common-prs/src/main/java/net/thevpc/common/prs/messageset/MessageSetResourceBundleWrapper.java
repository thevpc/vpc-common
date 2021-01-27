/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/

package net.thevpc.common.prs.messageset;

import java.util.*;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 13 juil. 2006 22:14:21
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
