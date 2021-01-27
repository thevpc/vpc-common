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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 23 juin 2006 17:26:30
 */
public class DefaultIconSetFactory implements IconSetFactory {

    private LinkedHashMap<String, DescAndLogger> setIdsToUrl = new LinkedHashMap<String, DescAndLogger>();

    private HashMap<IconSetIdPlusLocale, IconSet> iconSets = new HashMap<IconSetIdPlusLocale, IconSet>();

    public IconSet getIconSet(String iconSetName, Locale locale) throws IconSetNotFoundException {
        return getIconSet(iconSetName, locale, true);
    }

    public void registerIconSetDescriptor(IconSetDescriptor desc, LoggerProvider loggerProvider) {
//        desc.createIconSet(locale,loggerProvider);//try to create instance
        setIdsToUrl.put(desc.getId(), new DescAndLogger(desc, loggerProvider));
    }

    public IconSetDescriptor[] getIconSetDescriptors() {
        ArrayList<IconSetDescriptor> all = new ArrayList<IconSetDescriptor>(setIdsToUrl.size());
        for (DescAndLogger v : setIdsToUrl.values()) {
            all.add(v.desc);
        }
        return all.toArray(new IconSetDescriptor[all.size()]);
    }

    public IconSet getIconSet(String iconSetName, Locale locale, boolean autocreate) throws IconSetNotFoundException {
        DescAndLogger descAndLogger = setIdsToUrl.get(iconSetName);
        if (descAndLogger == null) {
            throw new IconSetNotFoundException(iconSetName);
        }
        IconSetDescriptor desc = descAndLogger.desc;
        IconSetIdPlusLocale key = new IconSetIdPlusLocale(iconSetName, locale == null ? null : locale.toString());
        IconSet iconSetObject = iconSets.get(key);
        if (iconSetObject == null) {
            if (locale != null) {
                try {
                    iconSetObject = getIconSet(iconSetName, null, false);
                } catch (IconSetNotFoundException e) {
                    ///
                }
            }
            if (iconSetObject == null) {
                if (iconSetName != null) {
                    try {
                        iconSetObject = getIconSet(null, locale, false);
                    } catch (IconSetNotFoundException e) {
                        //
                    }
                }
            }
        }
        if (iconSetObject == null && !autocreate) {
            return null;
        }

        if (iconSetObject == null) {
            iconSetObject = desc.createIconSet(locale, descAndLogger.loggerProvider);
            iconSets.put(key, iconSetObject);
        }
        return iconSetObject;
    }

    private static class IconSetIdPlusLocale {

        private String iconSetId;
        private String locale;

        public IconSetIdPlusLocale(String iconSetId, String locale) {
            this.iconSetId = iconSetId;
            this.locale = locale;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final IconSetIdPlusLocale iconSetIdPlusLocale = (IconSetIdPlusLocale) o;

            if (locale != null ? !locale.equals(iconSetIdPlusLocale.locale) : iconSetIdPlusLocale.locale != null) {
                return false;
            }
            if (iconSetId != null ? !iconSetId.equals(iconSetIdPlusLocale.iconSetId) : iconSetIdPlusLocale.iconSetId != null) {
                return false;
            }

            return true;
        }

        public int hashCode() {
            int result;
            result = (iconSetId != null ? iconSetId.hashCode() : 0);
            result = 29 * result + (locale != null ? locale.hashCode() : 0);
            return result;
        }
    }

    private static class DescAndLogger {

        IconSetDescriptor desc;
        LoggerProvider loggerProvider;

        public DescAndLogger(IconSetDescriptor desc, LoggerProvider loggerProvider) {
            this.desc = desc;
            this.loggerProvider = loggerProvider;
        }
    }
}
