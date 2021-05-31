package net.thevpc.common.props.impl;


import java.util.ArrayList;

import net.thevpc.common.props.*;

public class WritableListImpl<T> extends WritableListBase<T> {
    private java.util.List<T> value;

    public WritableListImpl(String name, PropertyType elementType) {
        this(name,elementType,new ArrayList());
    }

    public WritableListImpl(String name, PropertyType elementType, java.util.List value) {
        super(name, elementType);
        this.value = value;
    }

    @Override
    protected boolean addImpl(int index, T v) {
        value.add(index,v);
        return true;
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
    protected T removeAtImpl(int index) {
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



//    @Override
//    public <X> ObservableValue<X> mapAll(PropertyType toType, Function<T, X> mapper) {
//        return new MappedValueBase<T,X>(name(), toType,this,mapper);
//    }
}
