package net.thevpc.common.props.impl;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.PropertyAdjusters;
import net.thevpc.common.props.PropertyVetos;
import net.thevpc.common.props.WritableValue;

public class WritableValueDelegate<T> extends ObservableValueDelegate<T> implements WritableValue<T>{
    public WritableValueDelegate(ObservableValue<T> base) {
        super(base);
    }

    @Override
    protected WritableValue<T> getBase() {
        return (WritableValue<T>)super.getBase();
    }

    @Override
    public PropertyAdjusters adjusters() {
        return getBase().adjusters();
    }

    @Override
    public PropertyVetos vetos() {
        return getBase().vetos();
    }

    @Override
    public void set(T v) {
        getBase().set(v);
    }
}
