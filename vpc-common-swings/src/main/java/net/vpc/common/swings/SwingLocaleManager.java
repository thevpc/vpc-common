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
package net.vpc.common.swings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) @creationtime 13 juil. 2006
 * 22:14:21
 */
public class SwingLocaleManager {

    public static final String PREFIX = "SwingLocaleManager.";

    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
        sun.awt.AppContext.getAppContext().put("JComponent.defaultLocale", locale);
        UIManager.getDefaults().setDefaultLocale(locale);
    }

    public static void setComponentLocale(JComponent component, Locale locale) {
        component.putClientProperty(PREFIX + "Locale", locale);
    }

    public static Locale getComponentLocaleDeclaration(JComponent component) {
        return (Locale) component.getClientProperty(PREFIX + "Locale");
    }

    public static Locale getComponentLocale(JComponent component) {
        Locale lo = getComponentLocaleDeclaration(component);
        Component cc = component;
        while (lo == null && cc != null) {
            if (cc instanceof JComponent) {
                lo = getComponentLocaleDeclaration((JComponent) cc);
            }
            cc = cc.getParent();
        }
        return lo == null ? Locale.getDefault() : lo;
    }

    public static void setActionLocale(Action component, Locale locale) {
        component.putValue(PREFIX + "Locale", locale);
    }

    public static Locale getActionLocaleDeclaration(Action action) {
        return (Locale) action.getValue(PREFIX + "Locale");
    }

    public static Locale getActionLocale(Action action) {
        Locale lo = getActionLocaleDeclaration(action);
        return lo == null ? Locale.getDefault() : lo;
    }
}
