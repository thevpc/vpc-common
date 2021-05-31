package net.thevpc.common.props.impl;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.Property;

public class ObservableValueDelegate<T> extends PropertyDelegate implements ObservableValue<T> {
    public ObservableValueDelegate(ObservableValue<T> base) {
        super(base);
    }
    protected ObservableValue<T> getBase() {
        return (ObservableValue<T>) super.getBase();
    }

    @Override
    public T get() {
        return getBase().get();
    }

    @Override
    public ObservableValue<T> readOnly() {
        return this;
    }
}
