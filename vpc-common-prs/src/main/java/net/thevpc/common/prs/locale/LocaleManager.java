/**
 * ==================================================================== vpc-prs
 * library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
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
