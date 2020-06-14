/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.i18n;

import java.util.ResourceBundle;
import net.vpc.common.i18n.I18nBundle;

/**
 *
 * @author vpc
 */
public class I18nResourceBundle implements I18nBundle {

    private ResourceBundle bundle;

    public I18nResourceBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    @Override
    public String getString(String name) {
        return bundle.getString(name);
    }
}
