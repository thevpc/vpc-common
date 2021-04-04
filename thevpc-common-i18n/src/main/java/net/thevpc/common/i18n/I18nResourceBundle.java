/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author thevpc
 */
public class I18nResourceBundle implements I18nBundle {

    private Map<String,ResourceBundle> bundles=new HashMap<>();
    private String bunldeLocation;
    public I18nResourceBundle(String bundle) {
        this.bunldeLocation=bundle;
        resolve(null);
    }
    
    private ResourceBundle resolve(Locale locale) {
        if(locale==null){
            locale=Locale.getDefault();
        }
        String locId = locale.toString();
        ResourceBundle b = bundles.get(locId);
        if(b==null){
            b=ResourceBundle.getBundle(bunldeLocation,locale);
            bundles.put(locId, b);
        }
        return b;
    }
    
    @Override
    public String getString(String name,Locale locale) {
        return resolve(locale).getString(name);
    }
}
