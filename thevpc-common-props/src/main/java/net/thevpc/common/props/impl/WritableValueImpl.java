package net.thevpc.common.props.impl;
import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.ObservableValue;

public class WritableValueImpl<T> extends WritableValueBase<T>{

    public WritableValueImpl(String name, PropertyType type, T value) {
        super(name, type, value);
    }
}
