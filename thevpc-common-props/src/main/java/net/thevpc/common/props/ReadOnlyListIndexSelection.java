package net.thevpc.common.props;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ReadOnlyListIndexSelection<T> implements ObservableListIndexSelection<T> {
    protected ObservableListIndexSelection<T> base;

    public ReadOnlyListIndexSelection(ObservableListIndexSelection<T> base) {
        this.base = base;
    }

    @Override
    public ObservableList<Integer> indices() {
        return base.indices().readOnly();
    }

    @Override
    public T get() {
        return base.get();
    }

    @Override
    public T get(int index) {
        return base.get(index);
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public int[] findAllIndexes(Predicate<T> a) {
        return base.findAllIndexes(a);
    }

    @Override
    public boolean contains(T a) {
        return base.contains(a);
    }

    @Override
    public List<T> findAll(Predicate<T> a) {
        return base.findAll(a);
    }

    @Override
    public T findFirst(Predicate<T> a) {
        return base.findFirst(a);
    }

    @Override
    public T findFirst(Predicate<T> a, int from) {
        return base.findFirst(a);
    }

    @Override
    public T findFirst(Predicate<T> a, int from, int to) {
        return base.findFirst(a,from,to);
    }

    @Override
    public int findFirstIndex(Predicate<T> a) {
        return base.findFirstIndex(a);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from) {
        return base.findFirstIndex(a,from);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from, int to) {
        return base.findFirstIndex(a,from,to);
    }

    @Override
    public List<T> toList() {
        return base.toList();
    }

    @Override
    public ObservableListIndexSelection<T> readOnly() {
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return ReadOnlyIterator.of(base.iterator()) ;
    }

    @Override
    public String propertyName() {
        return base.propertyName();
    }

    @Override
    public PropertyListeners events() {
        return base.events();
    }

    @Override
    public PropertyType propertyType() {
        return base.propertyType();
    }

    @Override
    public UserObjects userObjects() {
        return base.userObjects();
    }

//    @Override
//    public ObservableValue<Boolean> multipleSelection() {
//        return base.multipleSelection();
//    }
}
