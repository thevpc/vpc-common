package net.vpc.common.props.impl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.vpc.common.props.*;

public abstract class AbstractWritablePListImpl<T> extends AbstractProperty implements WritablePList<T> {

    private PList<T> ro;

    public AbstractWritablePListImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType));
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>(size());
        for (T a : this) {
            list.add(a);
        }
        return list;
    }

    @Override
    public void removeAll() {
        while (size() > 0) {
            remove(0);
        }
    }

    @Override
    public void removeAll(Predicate<T> a) {
        while (size() > 0) {
            int u = findFirstIndex(a);
            if (u < 0) {
                break;
            }
            remove(u);
        }
    }

    @Override
    public void add(int index, T v) {
        addImpl(index, v);
        if (v instanceof WithListeners) {
            listeners.addDelegate((WithListeners) v, () -> "/" + index);
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                index,
                null,
                v,
                "/" + index,
                PropertyUpdate.ADD
        ));
    }

    @Override
    public void add(T v) {
        add(size(), v);
    }

    @Override
    public void set(int index, T v) {
        T old = setImpl(index, v);
        if (old instanceof WithListeners) {
            listeners.removeDelegate((WithListeners) old);
        }
        if (v instanceof WithListeners) {
            listeners.addDelegate((WithListeners) v, () -> "/" + index);
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                index,
                old,
                v,
                "/" + index,
                PropertyUpdate.UPDATE
        ));
    }

    @Override
    public T remove(int index) {
        T old = removeImpl(index);
        if (old instanceof WithListeners) {
            listeners.removeDelegate((WithListeners) old);
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                index,
                old,
                null,
                "/" + index,
                PropertyUpdate.REMOVE
        ));
        return old;
    }

    @Override
    public boolean remove(T item) {
        int i = indexOfImpl(item);
        if (i >= 0) {
            remove(i);
            return true;
        }
        return false;
    }

    @Override
    public PList<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyPList<>(this);
        }
        return ro;
    }

    protected abstract void addImpl(int index, T v);

    protected abstract T setImpl(int index, T v);

    protected abstract int indexOfImpl(T v);

    protected abstract T removeImpl(int index);

    protected abstract T getImpl(int index);

    protected abstract int sizeImpl();

    @Override
    public T get(int index) {
        return getImpl(index);
    }

    @Override
    public int size() {
        return sizeImpl();
    }

    @Override
    public int[] findAllIndexes(Predicate<T> a) {
        List<Integer> all = new ArrayList<>();
        int i = 0;
        while (true) {
            int u = findFirstIndex(a, i);
            if (u < 0) {
                break;
            }
            all.add(u);
            i = u + 1;
        }
        return all.stream().mapToInt(x -> x).toArray();
    }

    @Override
    public List<T> findAll(Predicate<T> a) {
        List<T> all = new ArrayList<>();
        int i = 0;
        while (true) {
            int u = findFirstIndex(a, i);
            if (u < 0) {
                break;
            }
            all.add(get(u));
            i = u + 1;
        }
        return all;
    }

    @Override
    public T findFirst(Predicate<T> a) {
        int i = findFirstIndex(a);
        return i >= 0 ? get(i) : null;
    }

    //    @Override
//    public java.util.List get() {
//        return value == null ? null : Collections.unmodifiableList(value);
//    }
    //    @Override
//    public void set(java.util.List v) {
//        if (!isRefWritable()) {
//            throw new IllegalArgumentException("Read only " + name());
//        }
//        this.value = v;
//    }
    @Override
    public T findFirst(Predicate<T> a, int from) {
        int i = findFirstIndex(a, from);
        return i >= 0 ? get(i) : null;
    }

    @Override
    public T findFirst(Predicate<T> a, int from, int to) {
        int i = findFirstIndex(a, from, to);
        return i >= 0 ? get(i) : null;
    }

    @Override
    public int findFirstIndex(Predicate<T> a) {
        for (int i = 0; i < size(); i++) {
            if (a.test(get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from) {
        int size = size();
        if (from > size) {
            from = size;
        }
        if (from < 0) {
            from = 0;
        }
        for (int i = from; i < size(); i++) {
            if (a.test(get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from, int to) {
        int size = size();
        if (to > size) {
            to = size;
        }
        if (to < 0) {
            to = 0;
        }

        if (from > size) {
            from = size;
        }
        if (from < 0) {
            from = 0;
        }

        for (int i = from; i < size(); i++) {
            if (a.test(get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public T next() {
                T u = get(index);
                index++;
                return u;
            }

            @Override
            public void remove() {
                AbstractWritablePListImpl.this.remove(index - 1);
            }
        };
    }

    @Override
    public String toString() {
        return "WritablePList{"
                + "name='" + name() + '\''
                + ", type=" + type()
                + " value=[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + '}';
    }

}
