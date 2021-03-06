package net.thevpc.common.props.impl;


import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.thevpc.common.props.ObservableStack;

public class ReadOnlyStack<T> extends PropertyDelegate implements ObservableStack<T> {

    public ReadOnlyStack(ObservableStack<T> base) {
        super(base);
    }

    @Override
    public ObservableStack<T> getBase() {
        return (ObservableStack<T>) super.getBase();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public T peek() {
        return getBase().peek();
    }

    @Override
    public T peek(int index) {
        return getBase().peek(index);
    }

    @Override
    public ObservableStack<T> readOnly() {
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
    public boolean isEmpty() {
        return getBase().isEmpty();
    }

    @Override
    public int size() {
        return getBase().size();
    }

    @Override
    public int[] findAllIndexes(Predicate<T> a) {
        return getBase().findAllIndexes(a);
    }

    @Override
    public List<T> findAll(Predicate<T> a) {
        return getBase().findAll(a);
    }

    @Override
    public T findFirst(Predicate<T> a) {
        return getBase().findFirst(a);
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
    public int findFirstIndex(Predicate<T> a) {
        return getBase().findFirstIndex(a);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from) {
        return getBase().findFirstIndex(a, from);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from, int to) {
        return getBase().findFirstIndex(a, from, to);
    }

    public Iterator<T> iterator() {
        return getBase().iterator();
    }
}
