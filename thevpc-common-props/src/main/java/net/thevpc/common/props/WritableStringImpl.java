/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import net.thevpc.common.props.impl.WritableValueImpl;

/**
 *
 * @author thevpc
 */
public class WritableStringImpl extends WritableValueImpl<String> implements WritableString{

    public WritableStringImpl(String name, String value) {
        super(name, PropertyType.of(String.class),
                value
        );
    }
}
