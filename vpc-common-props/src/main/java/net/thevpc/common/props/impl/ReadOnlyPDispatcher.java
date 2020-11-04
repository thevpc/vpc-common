package net.thevpc.common.props.impl;

import net.thevpc.common.props.PDispatcher;
import net.thevpc.common.props.*;

public class ReadOnlyPDispatcher<T> extends DelegateProperty implements PDispatcher<T> {

    public ReadOnlyPDispatcher(PDispatcher<T> base) {
        super(base);
    }

    @Override
    public PDispatcher<T> getBase() {
        return (PDispatcher<T>) super.getBase();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public PDispatcher<T> readOnly() {
        return this;
    }

}
