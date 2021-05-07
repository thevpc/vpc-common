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
package net.thevpc.common.swing;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 13 juil. 2006
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
