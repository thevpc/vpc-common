package net.vpc.common.prpbind;


import net.vpc.common.prpbind.impl.WritablePListImpl;

import java.util.ArrayList;
import java.util.function.Predicate;

public interface WritablePList<T> extends PList<T> {

    void removeAll();

    void removeAll(Predicate<T> a);

    void add(int index, T v);

    void add(T v);

    void set(int index, T v);

    void remove(int index);

    boolean remove(T item);

    PList<T> readOnly();

    PropertyVetos vetos();

}
