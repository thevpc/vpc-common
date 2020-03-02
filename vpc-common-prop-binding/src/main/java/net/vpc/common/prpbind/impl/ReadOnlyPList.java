package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.PList;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ReadOnlyPList<T> extends DelegateProperty implements PList<T> {
    public ReadOnlyPList(PList<T> base) {
        super(base);
    }

    @Override
    public PList<T> getBase() {
        return (PList<T>) super.getBase();
    }

    @Override
    public boolean isRefWritable() {
        return false;
    }

    @Override
    public boolean isValueWritable() {
        return false;
    }


//    //    @Override
//    public PList<T> get() {
//        return value == null ? null : Collections.unmodifiableList(value);
//    }
//
//    //    @Override
//    public void set(PList<T> v) {
//        if (!isRefWritable()) {
//            throw new IllegalArgumentException("Read only " + getName());
//        }
//        this.value = v;
//    }


    @Override
    public int[] findAllIndexes(Predicate<T> a) {
        return getBase().findAllIndexes(a);
    }

    @Override
    public List<T> findAll(Predicate<T> a) {
        return getBase().findAll(a);
    }

    public Iterator<T> iterator() {
        return getBase().iterator();
    }

    @Override
    public T findFirst(Predicate<T> a) {
        return getBase().findFirst(a);
    }

    @Override
    public int findFirstIndex(Predicate<T> a) {
        return getBase().findFirstIndex(a);
    }

    @Override
    public T get(int index) {
        return getBase().get(index);
    }

    @Override
    public int size() {
        return getBase().size();
    }

    @Override
    public T findFirst(Predicate<T> a, int from) {
        return getBase().findFirst(a, from);
    }

    @Override
    public T findFirst(Predicate<T> a, int from, int to) {
        return getBase().findFirst(a, from, to);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from) {
        return getBase().findFirstIndex(a, from);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from, int to) {
        return getBase().findFirstIndex(a, from, to);
    }
}