package net.vpc.common.props.impl;

import net.vpc.common.props.PDispatcher;
import net.vpc.common.props.*;

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
