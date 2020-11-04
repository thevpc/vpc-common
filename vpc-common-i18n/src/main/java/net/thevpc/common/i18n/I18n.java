/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import java.util.function.Function;
import net.thevpc.common.props.WritablePList;
import net.thevpc.common.props.WritablePValue;

/**
 *
 * @author vpc
 */
public interface I18n {

    public WritablePList<I18nBundle> bundles();

    public String getString(String name);

    public String getString(String name, Function<String, String> defaultValue);

    WritablePValue<Function<String, String>> defaultValue();

}
