package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.PValue;

public class ReadOnlyPValue<T> extends DelegateProperty<T> implements PValue<T> {
    public ReadOnlyPValue(PValue<T> v) {
        super(v);
    }

    @Override
    public PValue<T> getBase() {
        return (PValue<T>) super.getBase();
    }

//    public static <T> ReadOnlyPValue<T> of(PValue<T> v) {
//        if (v instanceof ReadOnlyPValue<?>) {
//            return (ReadOnlyPValue<T>) v;
//        }else{
//            if(ro==null){
//        }
//        return new ReadOnlyPValue<T>(v);
//    }

    @Override
    public T get() {
        return getBase().get();
    }
}
