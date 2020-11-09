/**
 * ==================================================================== vpc-prs
 * library
 *
 * Description: <start><end>
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
package net.thevpc.common.prs.locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) @creationtime 13 juil. 2006
 * 22:14:21
 */
public class LocaleManager {

    private static LocaleManager instance = new LocaleManager();

    public static LocaleManager getInstance() {
        return instance;
    }
    private List<LocaleManagerListener> listeners;
    private Locale locale;
    private ArrayList<Locale> availableLocales = new ArrayList<Locale>();
//    public static final String PREFIX = "SwingLocaleManager.";

    public void setLocale(Locale locale) {
//        Locale.setDefault(locale);
//        sun.awt.AppContext.getAppContext().put("JComponent.defaultLocale", locale);
//        UIManager.getDefaults().setDefaultLocale(locale);
        Locale old = this.locale;
        if ((old == null && locale != null) || (old != null && !old.equals(locale))) {
            this.locale = locale;
            if (listeners != null) {
                for (LocaleManagerListener localeManagerListener : listeners) {
                    localeManagerListener.localeChanged(this, old, locale);
                }
            }
        }
    }
    
    public void addLocaleManagerListener(LocaleManagerListener listener){
        if(listeners==null){
            listeners= new ArrayList<LocaleManagerListener>();
        }
        listeners.add(listener);
    }

    public void removeLocaleManagerListener(LocaleManagerListener listener){
        if(listeners!=null){
            listeners.remove(listener);
        }
    }

    public void registerLocale(Locale locale) {
        if (locale != null && !availableLocales.contains(locale)) {
            availableLocales.add(locale);
            if (listeners != null) {
                for (LocaleManagerListener localeManagerListener : listeners) {
                    localeManagerListener.localeAdded(this, locale);
                }
            }
        }
    }

    public Locale[] getAvailableLocales() {
        return availableLocales.toArray(new Locale[availableLocales.size()]);
    }
}
