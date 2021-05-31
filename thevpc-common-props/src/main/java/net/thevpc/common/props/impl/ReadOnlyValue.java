package net.thevpc.common.props.impl;

import net.thevpc.common.props.ObservableValue;

public class ReadOnlyValue<T> extends ObservableValueDelegate<T>{

    public ReadOnlyValue(ObservableValue<T> v) {
        super(v);
    }
    @Override
    public ObservableValue<T> readOnly() {
        return this;
    }
}
