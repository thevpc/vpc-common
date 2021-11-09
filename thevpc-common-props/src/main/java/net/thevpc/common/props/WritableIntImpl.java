/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import net.thevpc.common.props.impl.WritableValueImpl;

/**
 * @author thevpc
 */
public class WritableIntImpl extends WritableValueImpl<Integer> implements WritableInt{

    public WritableIntImpl(String name, boolean nullable, Integer value) {
        super(name, PropertyType.of(nullable ? Integer.class : int.class),
                (!nullable && value == null) ? (Integer) 0 : value
        );
    }

    public void set(int value) {
        this.set((Integer) value);
    }

    public void inc(int value) {
        set(get() + value);
    }

    public void inc() {
        inc(1);
    }
}
