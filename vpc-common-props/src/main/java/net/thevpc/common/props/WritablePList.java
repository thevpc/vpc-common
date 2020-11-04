package net.thevpc.common.props;


import java.util.function.Predicate;

public interface WritablePList<T> extends PList<T> {

    void removeAll();

    void removeAll(Predicate<T> a);

    void add(int index, T v);

    void add(T v);

    void set(int index, T v);

    T remove(int index);

    boolean remove(T item);

    PropertyVetos vetos();

}
