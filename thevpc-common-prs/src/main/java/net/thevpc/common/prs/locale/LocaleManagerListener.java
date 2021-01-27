/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.locale;

import java.util.Locale;

/**
 *
 * @author thevpc
 */
public interface LocaleManagerListener {

    public void localeAdded(LocaleManager manager, Locale locale);

    public void localeRemoved(LocaleManager manager, Locale locale);

    public void localeChanged(LocaleManager manager, Locale oldLocale, Locale newLocale);
}
