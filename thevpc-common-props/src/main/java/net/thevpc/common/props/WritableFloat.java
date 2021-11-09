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
public class WritableFloat extends WritableValueImpl<Float> {

    public WritableFloat(String name, boolean nullable,Float value) {
        super(name, PropertyType.of(nullable?float.class:float.class), 
                (!nullable && value == null) ? (Float)0.0f : value
                );
    }

    public void set(float value) {
        this.set((Float) value);
    }
}
