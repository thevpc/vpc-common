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
public class WritableBooleanImpl extends WritableValueImpl<Boolean> implements WritableBoolean {

    public WritableBooleanImpl(String name, boolean nullable, Boolean value) {
        super(name, PropertyType.of(nullable ? Boolean.class : boolean.class),
                (!nullable && value == null) ? (Boolean)false : value
        );
    }

    public void set(boolean value) {
        this.set((Boolean) value);
    }

    public <T> void bindEquals(WritableValue<T> property, T value) {
        bind(
                property.mapEqualsValue(value),
                x->{
                    if(x) {
                        property.set(value);
                    }
                }
        );
    }
//
//    @Override
//    public WritableBoolean not() {
//        return mapBooleanValue(x->!x);
//    }

    @Override
    public ObservableBoolean readOnly() {
        return mapBooleanValue(x->x);
    }

}
