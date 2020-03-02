package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class WritablePListImpl<T> extends AbstractWritablePListImpl<T> implements WritablePList<T> {
    private java.util.List<T> value;

    public WritablePListImpl(String name, PropertyType elementType) {
        this(name,elementType,new ArrayList());
    }

    public WritablePListImpl(String name, PropertyType elementType, java.util.List value) {
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
