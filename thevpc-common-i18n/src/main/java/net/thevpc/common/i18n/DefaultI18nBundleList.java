/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.impl.WritableListAdapter;

/**
 *
 * @author thevpc
 */
public class DefaultI18nBundleList extends WritableListAdapter<I18nBundle> implements I18nBundleList {

    private WritableList<I18nBundle> ad;

    public DefaultI18nBundleList(String name) {
        ad = Props.of(name).listOf(I18nBundle.class);
    }

    @Override
    public void add(String resourceUrl) {
        this.add(new I18nResourceBundle(resourceUrl));
    }

    @Override
    protected WritableList<I18nBundle> getAdaptee() {
        return ad;
    }
}
