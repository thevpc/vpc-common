/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import net.thevpc.common.i18n.Str;
import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.impl.WritableValueImpl;

/**
 *
 * @author thevpc
 */
public class WritableStr extends WritableValueImpl<Str> {
    public WritableStr(String name) {
        super(name, PropertyType.of(Str.class), null);
    }
}
