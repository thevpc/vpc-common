package net.thevpc.common.props.impl;


import java.util.ArrayList;

import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.WritableList;

public class WritableListImpl<T> extends AbstractWritableListImpl<T> implements WritableList<T> {
    private java.util.List<T> value;

    public WritableListImpl(String name, PropertyType elementType) {
        this(name,elementType,new ArrayList());
    }

    public WritableListImpl(String name, PropertyType elementType, java.util.List value) {
        super(name, elementType);
        this.value = value;
    }

    @Override
    protected void addImpl(int index, T v) {
        value.add(index,v);
    }

    @Override
    protected T setImpl(int index, T v) {
        return value.set(index,v);
    }

    @Override
    protected int indexOfImpl(T v) {
        return value.indexOf(v);
    }

    @Override
    protected T removeImpl(int index) {
        return value.remove(index);
    }

    @Override
    protected T getImpl(int index) {
        return value.get(index);
    }

    @Override
    protected int sizeImpl() {
        return value.size();
    }
}
