package net.thevpc.common.props.impl;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.thevpc.common.props.ObservableList;

public class ReadOnlyList<T> extends DelegateProperty implements ObservableList<T> {

    public ReadOnlyList(ObservableList<T> base) {
        super(base);
    }

    @Override
    public boolean contains(T a) {
        return getBase().contains(a);
    }

    @Override
    public ObservableList<T> getBase() {
        return (ObservableList<T>) super.getBase();
    }

    @Override
    public List<T> toList() {
        return getBase().toList();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public ObservableList<T> readOnly() {
        return this;
    }

//    //    @Override
//    public PList<T> get() {
//        return value == null ? null : Collections.unmodifiableList(value);
//    }
//
//    //    @Override
//    public void set(PList<T> v) {
//        if (!isRefWritable()) {
//            throw new IllegalArgumentException("Read only " + name());
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
