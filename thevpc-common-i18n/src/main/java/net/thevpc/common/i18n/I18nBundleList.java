/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import net.thevpc.common.props.WritableList;

/**
 *
 * @author vpc
 */
public interface I18nBundleList extends WritableList<I18nBundle>{
    void add(String resourceUrl);
}
