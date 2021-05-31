package net.thevpc.common.props.impl;


import net.thevpc.common.props.PropertyType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class WritableSetImpl<T> extends WritableListBase<T> {
    private java.util.List<T> values;
//    private java.util.Set<T> set=new HashSet<>();

    public WritableSetImpl(String name, PropertyType elementType) {
        this(name,elementType,new ArrayList());
    }

    public WritableSetImpl(String name, PropertyType elementType, java.util.List values) {
        super(name, elementType);
        this.values = values;
    }
    protected boolean canAddImpl(int index, T v){
//        if (set.contains(v)) {
        if (values.contains(v)) {
            return false;
        }
        return super.canAddImpl(index, v);
    }

    @Override
    protected boolean addImpl(int index, T v) {
        values.add(index,v);
//        set.add(v);
//        _check();
        return true;
    }

    @Override
    public void set(int index, T v) {
        T old = get(index);
        if(Objects.equals(v,old)){
            // fast exit!
            return;
        }
        if(contains(v)){
            removeAt(index);
        }else{
            super.set(index,v);
        }
//        _check();
    }

    @Override
    public boolean contains(T a) {
        return values.contains(a);
    }

    @Override
    protected T setImpl(int index, T v) {
//        set.add(v);
        T r = values.set(index, v);
//        set.remove(r);
//        _check();
        return r;
    }

//    protected void _check(){
//        if(set.size()!=value.size()){
//            throw new IllegalArgumentException("invlaid");
//        }
//    }

    @Override
    protected int indexOfImpl(T v) {
        return values.indexOf(v);
    }

    @Override
    protected T removeAtImpl(int index) {
        T v = values.remove(index);
//        if(!set.remove(v)){
//            set.remove(v);
//        }
//        _check();
        return v;
    }

    @Override
    protected T getImpl(int index) {
        return values.get(index);
    }

    @Override
    protected int sizeImpl() {
        return values.size();
    }

    @Override
    public String toString() {
        String p=isWritable()?"Writable":"ReadOnly";
        return p+"Set("+fullPropertyName()+"){"
                + " values=[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + '}';
    }


//    @Override
//    public <X> ObservableValue<X> mapAll(PropertyType toType, Function<T, X> mapper) {
//        return new MappedValueBase<T,X>(name(), toType,this,mapper);
//    }
}
