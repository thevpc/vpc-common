package net.thevpc.common.props;

import java.util.List;
import java.util.function.Predicate;

public interface PStack<T> extends Property, Iterable<T> {

    T peek();

    T peek(int index);

    boolean isEmpty();

    int size();

    int[] findAllIndexes(Predicate<T> a);

    List<T> findAll(Predicate<T> a);

    T findFirst(Predicate<T> a);

    T findFirst(Predicate<T> a, int from);

    T findFirst(Predicate<T> a, int from, int to);

    int findFirstIndex(Predicate<T> a);

    int findFirstIndex(Predicate<T> a, int from);

    int findFirstIndex(Predicate<T> a, int from, int to);

    PStack<T> readOnly();
}
