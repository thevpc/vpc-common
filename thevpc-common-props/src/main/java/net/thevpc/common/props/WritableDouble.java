/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import net.thevpc.common.props.impl.WritableValueImpl;

/**
 *
 * @author vpc
 */
public class WritableDouble extends WritableValueImpl<Double> {

    public WritableDouble(String name, boolean nullable, Double value) {
        super(name, PropertyType.of(nullable ? Double.class : double.class),
                (!nullable && value == null) ? (Double)0.0 : value
        );
    }

    public void set(double value) {
        this.set((Double) value);
    }
}
