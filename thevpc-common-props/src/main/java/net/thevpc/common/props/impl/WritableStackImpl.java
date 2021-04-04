package net.thevpc.common.props.impl;


import java.util.*;
import java.util.function.Predicate;

import net.thevpc.common.props.*;
import net.thevpc.common.props.*;

public class WritableStackImpl<T> extends AbstractProperty implements WritableStack<T> {

    private Stack<T> value = new Stack<>();
    private ObservableStack<T> ro;

    public WritableStackImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(Stack.class, elementType));
    }

    @Override
    public T popIf(Predicate<T> a) {
        if (!isEmpty()) {
            T p = peek();
            if (a.test(p)) {
                pop();
                return p;
            }
        }
        return null;
    }

    @Override
    public void push(T v) {
        value.push(v);
        if (v instanceof WithListeners) {
            listeners.removeDelegate((WithListeners) v);
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                value.size(),
                null,
                v,
                "/" + (value.size()),
                PropertyUpdate.ADD
        ));
    }

    @Override
    public T pop() {
        T p = value.pop();
        if (p instanceof WithListeners) {
            listeners.removeDelegate((WithListeners) p);
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                value.size(),
                p,
                null,
                "/" + (value.size()),
                PropertyUpdate.REMOVE
        ));
        return p;
    }

    @Override
    public void popAll() {
        while (isEmpty()) {
            pop();
        }
    }

    @Override
    public ObservableStack<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyStack<>(this);
        }
        return ro;
    }

    public T remove(int index) {
        T p = value.remove(index);
        if (p instanceof WithListeners) {
            listeners.removeDelegate((WithListeners) p);
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                index,
                p,
                null,
                "/" + index,
                PropertyUpdate.REMOVE
        ));
        return p;
    }

    @Override
    public T peek() {
        return value.pop();
    }

    @Override
    public T peek(int index) {
        return value.get(value.size() - 1 - index);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public int size() {
        return value.size();
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

    @Override
    public T findFirst(Predicate<T> a, int from) {
        int i = findFirstIndex(a, from);
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

    //    @Override
    public T get(int index) {
        return value.get(index);
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    public Iterator<T> iterator() {
        return Collections.unmodifiableList(value).iterator();
    }

    @Override
    public String toString() {
        return "WritablePStack{"
                + "name='" + name() + '\''
                + ", type=" + type()
                + " value='" + value + '\''
                + '}';
    }

}
