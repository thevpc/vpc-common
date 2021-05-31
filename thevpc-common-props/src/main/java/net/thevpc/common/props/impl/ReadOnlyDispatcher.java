package net.thevpc.common.props.impl;

import net.thevpc.common.props.ObservableDispatcher;

public class ReadOnlyDispatcher<T> extends PropertyDelegate implements ObservableDispatcher<T> {

    public ReadOnlyDispatcher(ObservableDispatcher<T> base) {
        super(base);
    }

    @Override
    public ObservableDispatcher<T> getBase() {
        return (ObservableDispatcher<T>) super.getBase();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public ObservableDispatcher<T> readOnly() {
        return this;
    }

}
