package net.thevpc.common.props.impl;

import net.thevpc.common.props.WritableValueModel;

public class DefaultWritableValueModel<T> implements WritableValueModel<T> {
    private T value;

    public DefaultWritableValueModel(T value) {
        this.value = value;
    }

    @Override
    public void set(T value) {
        this.value=value;
    }

    @Override
    public T get() {
        return value;
    }
}
