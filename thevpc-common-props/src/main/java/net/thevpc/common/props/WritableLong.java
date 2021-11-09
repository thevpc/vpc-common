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
public class WritableLong extends WritableValueImpl<Long> {

    public WritableLong(String name, boolean nullable, Long value) {
        super(name, PropertyType.of(nullable ? Long.class : long.class),
                (!nullable && value == null) ? (Long)0L : value
        );
    }

    public void set(long value) {
        this.set((Long) value);
    }
}
