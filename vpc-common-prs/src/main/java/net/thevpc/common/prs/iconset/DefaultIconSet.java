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
package net.thevpc.common.prs.iconset;

import net.thevpc.common.prs.messageset.MessageSetBundle;
import net.thevpc.common.prs.messageset.MessageSetResourceBundleWrapper;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 23 juin 2006 17:26:21
 */
public class DefaultIconSet extends AbstractIconSet {

    private String baseUrl;
    private String criteria;
    private MessageSetBundle bundle;
    private ArrayList<MessageSetBundle> imported;
    private ClassLoader resourceLoader;

//    public DefaultIconSet() {
//    }
    public DefaultIconSet(String path, ClassLoader loader, Locale locale, LoggerProvider loggerProvider) {
        this(new IconSetDescriptor(path, null, loader == null ? DefaultIconSet.class.getClassLoader() : loader),locale, loggerProvider);
    }
    
    public DefaultIconSet(IconSetDescriptor desc, Locale locale, LoggerProvider loggerProvider) {
        super();
        setId(desc.getId());
        setLoggerProvider(loggerProvider);
        locale = locale == null ? Locale.getDefault() : locale;
        setLocale(locale);
        baseUrl = desc.getInitialParameters()[0];
        setResourceLoader(desc.getClassLoader());
        init(new MessageSetResourceBundleWrapper(baseUrl, getLocale(), desc.getClassLoader()));
        String localizedName=null;
        try {
            localizedName = getBundle().getString("icon-set.name");
        } catch (MissingResourceException e) {
            localizedName = desc.getName();
            if (localizedName == null) {
                localizedName = desc.getId();
            }
            if (localizedName == null) {
                localizedName = baseUrl;
            }
//            e.printStackTrace();
        }
        setName(localizedName);
    }

    private static String parseIdFromPath(String path) {
        String[] s = path.split("\\.");
        return s[s.length - 1];
    }

    public final void init(MessageSetBundle b) {
        bundle = b;
        try {
            setUnknownIconPath(bundle.getString("icon-set.unknown"));
        } catch (MissingResourceException e) {
//            e.printStackTrace();
        }
        try {
            setDefaultIconPath(bundle.getString("icon-set.default"));
        } catch (MissingResourceException e) {
//            e.printStackTrace();
        }

        try {
            criteria = getBundle().getString("icon-set-criteria");
        } catch (MissingResourceException e) {
            criteria = getUnknownIconPath();
        }
        if (criteria != null) {
            for (String s : criteria.split(";")) {
                if (s.length() > 0) {
                    Icon ico = null;
                    try {
                        ico = getUnknowIcon();
                        if (ico != null && ico.getIconHeight() <= 0) {
                            ico = null;
                        }
                    } catch (Throwable e) {
                        //
                    }
                    if (ico == null) {
                        throw new InvalidIconSetException("icon-set-criteria unsatisfied; ignored. : " + s);
                    }
                }
            }
        }

        String[] imports = null;
        try {
            String importedString = bundle.getString("icon-set.imports");
            if (importedString.trim().length() > 0) {
                imports = importedString.split(";");
            }
        } catch (MissingResourceException e) {
            //e.printStackTrace();
        }
        if (imports != null) {
            for (String string : imports) {
                try {
                    ClassLoader cl = IconSetManager.getClassLoaderForIconSetImports(string.trim());
                    add(new MessageSetResourceBundleWrapper(
                            string.trim(),
                            getLocale(),
                            cl == null ? getResourceLoader() : cl));
                } catch (MissingResourceException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void add(MessageSetBundle other) {
        if (other != null) {
            if (imported == null) {
                imported = new ArrayList<MessageSetBundle>();
            }
            imported.add(other);
        }
    }

    public void remove(MessageSetBundle other) {
        if (other != null && imported != null) {
            imported.remove(other);
        }

    }

    public ClassLoader getResourceLoader() {
        return resourceLoader;
    }

    public final void setResourceLoader(ClassLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Icon loadImageIcon(String path) {
        if (path == null) {
            return null;
        }
        // UIManager Icon
        if (path.toLowerCase().startsWith("UIManager://".toLowerCase())) {
            return UIManager.getIcon(path.substring("UIManager://".length()));
        }
        // relative URL
        URL resource = getResourceLoader().getResource(path);
        if (resource == null) {
            try {
                // absolute URL
                resource = new URL(path);
            } catch (MalformedURLException e) {
                return null;
            }
        }
        ImageIcon imageIcon = new ImageIcon(resource);
        return imageIcon.getIconHeight() > 0 ? imageIcon : null;
    }

    public final void setLocale(Locale locale) {
        super.setLocale(locale);
        if (bundle != null) {
            bundle.setLocale(locale);
        }
        if (imported != null) {
            for (MessageSetBundle messageSetBundle : imported) {
                messageSetBundle.setLocale(locale);
            }
        }
    }

    public final MessageSetBundle getBundle() {
        return bundle;
    }

    public String getIconPath(String id) throws MissingResourceException {
        if (id == null) {
            return null;
        }
        MissingResourceException error;
        try {
            return bundle.getString(id);
        } catch (MissingResourceException e) {
            error = e;
        }
        if (error != null && imported != null) {
            for (MessageSetBundle resourceBundle : imported) {
                try {
                    return resourceBundle.getString(id);
                } catch (MissingResourceException e) {
                    if (error == null) {
                        error = e;
                    }
                }
            }
        }
        throw error;
    }

    public String getCriteria() {
        return criteria;
    }
}
