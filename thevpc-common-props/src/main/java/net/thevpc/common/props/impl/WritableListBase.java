package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class WritableListBase<T> extends WritableValueBase<T> implements WritableList<T> {

    protected ObservableList<T> ro;

    public WritableListBase(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType), new WritableValueModel<T>() {
            //empty model because we do not have access to this in the constructor!!
            @Override
            public T get() {
                return null;
            }

            @Override
            public void set(T v) {
                //
            }
        });
        model = new WritableValueModel<T>() {
            @Override
            public T get() {
                return get0();
            }

            @Override
            public void set(T v) {
                set0(v);
            }
        };
    }

    protected T get0() {
        return isEmpty() ? null : get(0);
    }

    protected void set0(T value) {
        if (value == null) {
            clear();
        } else {
            while (size() > 1) {
                removeAt(1);
            }
            if (size() == 1) {
                set(0, value);
            } else {
                add(value);
            }
        }
    }

    protected boolean canAddImpl(int index, T v) {
        return true;
    }

    protected boolean canSetImpl(int index, T v) {
        T old = get(index);
        if (Objects.equals(v, old)) {
            // fast exit!
            return false;
        }
        return true;
    }

    protected boolean canRemoveImpl(int index) {
        if (index < 0 || index >= size()) {
            return false;
        }
        return true;
    }

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
    public boolean contains(T a) {
        return findFirstIndex(x -> Objects.equals(x, a)) >= 0;
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
    public List<T> toList() {
        List<T> list = new ArrayList<>(size());
        for (T a : this) {
            list.add(a);
        }
        return list;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        while (size() > 0) {
            removeAt(0);
        }
    }

    @Override
    public void removeAll(Predicate<T> a) {
        while (size() > 0) {
            int u = findFirstIndex(a);
            if (u < 0) {
                break;
            }
            removeAt(u);
        }
    }

    @Override
    public boolean add(int index, T v) {
        if (!canAddImpl(index, v)) {
            return false;
        }
        int size0 = size();
        PropertyEvent event = new PropertyEvent(
                this,
                index,
                null,
                v,
                Path.root().append(String.valueOf(index)),
                PropertyUpdate.ADD, true
        );
        PropertyAdjusterContext q = adjusters.firePropertyUpdated(event);
        for (Runnable runnable : q.getBefore()) {
            runnable.run();
        }
        if (!q.isIgnore()) {
            vetos.firePropertyUpdated(q.event());
            int size1 = size();
            if (size1 != size0) {
                q = adjusters.firePropertyUpdated(event);
            }
            if (addImpl(index, v)) {
                if (v instanceof Property) {
                    listeners.addDelegate((Property) v, () -> Path.root().append(String.valueOf(index)));
                }
                firePropertyUpdated(q.event());
            } else {
                return false;
            }
        }
        for (Runnable runnable : q.getAfter()) {
            runnable.run();
        }
        return true;
    }

    protected void firePropertyUpdated(PropertyEvent e) {
        ((DefaultPropertyListeners)listeners).firePropertyUpdated(e);
    }

    @Override
    public boolean add(T v) {
        return add(size(), v);
    }

    @Override
    public boolean setCollection(Collection<? extends T> all) {
        int currSize = size();
        boolean b = false;
        Object[] extra = all.toArray();
        if (extra.length < currSize) {
            for (int i = currSize - 1; i >= extra.length; i--) {
                removeAt(i);
                b = true;
            }
        }
        int mm = Math.min(extra.length, currSize);
        for (int i = 0; i < mm; i++) {
            set(i, (T) extra[i]);
            b = true;
        }
        if (extra.length > currSize) {
            for (int i = currSize; i < extra.length; i++) {
                add((T) extra[i]);
                b = true;
            }
        }
        return b;
    }

    @Override
    public boolean addCollection(Collection<? extends T> elements) {
        return Helpers.WritableList_addCollection(this,elements);
    }

    @Override
    public boolean removeCollection(Collection<? extends T> extra) {
        boolean b = false;
        for (T t : extra) {
            b |= remove(t);
        }
        return b;
    }

    @Override
    public void set(int index, T v) {
        if (!canSetImpl(index, v)) {
            return;
        }
        T old = get(index);
        PropertyEvent event = new PropertyEvent(
                this,
                index,
                old,
                v,
                Path.root().append(String.valueOf(index)),
                PropertyUpdate.UPDATE, true
        );

        PropertyAdjusterContext q = adjusters.firePropertyUpdated(event);
        for (Runnable runnable : q.getBefore()) {
            runnable.run();
        }
        if (!q.isIgnore()) {
            vetos.firePropertyUpdated(q.event());
            setImpl(index, v);
            if (v instanceof Property) {
                //listeners.addDelegate((Property) v, () -> Path.root().append(String.valueOf(index)));
            }
            ((DefaultPropertyListeners)listeners).firePropertyUpdated(q.event());
        }
        for (Runnable runnable : q.getAfter()) {
            runnable.run();
        }
    }

    @Override
    public T removeAt(int index) {
        if (!canRemoveImpl(index)) {
            return null;
        }
        T old = get(index);
        PropertyEvent event = new PropertyEvent(
                this,
                index,
                old,
                null,
                Path.root().append(String.valueOf(index)),
                PropertyUpdate.REMOVE, true
        );

        PropertyAdjusterContext q = adjusters.firePropertyUpdated(event);
        for (Runnable runnable : q.getBefore()) {
            runnable.run();
        }
        if (!q.isIgnore()) {
            vetos.firePropertyUpdated(q.event());
            removeAtImpl(index);
            ((DefaultPropertyListeners)listeners).firePropertyUpdated(q.event());
        }
        for (Runnable runnable : q.getAfter()) {
            runnable.run();
        }
        return old;
    }

    @Override
    public boolean remove(T item) {
        int i = indexOfImpl(item);
        if (i >= 0) {
            removeAt(i);
            return true;
        }
        return false;
    }

    protected abstract boolean addImpl(int index, T v);

    protected abstract T setImpl(int index, T v);

    protected abstract int indexOfImpl(T v);

    protected abstract T removeAtImpl(int index);

    protected abstract T getImpl(int index);

    protected abstract int sizeImpl();

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public String toString() {
        String p = isWritable() ? "Writable" : "ReadOnly";
        return p + "List(" + fullPropertyName() + "){"
                + " values=[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + '}';
    }

    /**
     * override the set value because all events will be managed in the
     * add/remove by index
     *
     * @param value singleton or null
     */
    @Override
    public void set(T value) {
        model().set(value);
    }

    @Override
    public ObservableList<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyList<>(this);
        }
        return ro;
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
                WritableListBase.this.removeAt(index - 1);
            }
        };
    }
}
