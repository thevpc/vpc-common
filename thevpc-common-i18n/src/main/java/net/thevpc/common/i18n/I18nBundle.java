/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import java.util.Locale;

/**
 *
 * @author thevpc
 */
public interface I18nBundle {
    String getString(String name,Locale locale);
}
