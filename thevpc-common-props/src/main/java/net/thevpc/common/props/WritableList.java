package net.thevpc.common.props;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public interface WritableList<T> extends ObservableList<T>, WritableValue<T> {

    /**
     * convenient method to replace all the values of the list with a singleton
     * or null
     *
     * @param value singleton or null
     */
    void set(T value);

    boolean isEmpty();

    void clear();

    void removeAll(Predicate<T> a);

    default void removeLast() {
        removeAt(size() - 1);
    }

    default void removeFirst() {
        removeAt(0);
    }

    boolean add(int index, T v);

    boolean add(T v);

    boolean setCollection(Collection<? extends T> all);

    default boolean setAll(T... all) {
        return setCollection(Arrays.asList(all));
    }

    boolean addCollection(Collection<? extends T> all);

    default boolean addAll(T... all) {
        return addCollection(Arrays.asList(all));
    }

    boolean removeCollection(Collection<? extends T> all);

    default boolean removeAll(T... all) {
        return removeCollection(Arrays.asList(all));
    }

    void set(int index, T v);

    T removeAt(int index);

    boolean remove(T item);

    @Override
    PropertyVetos vetos();

}
