package net.vpc.common.props;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface PList<T> extends Property, Iterable<T> {

    T get(int index);

    int size();

    int[] findAllIndexes(Predicate<T> a);

    List<T> findAll(Predicate<T> a);

    T findFirst(Predicate<T> a);

    T findFirst(Predicate<T> a, int from);

    T findFirst(Predicate<T> a, int from, int to);

    int findFirstIndex(Predicate<T> a);

    int findFirstIndex(Predicate<T> a, int from);

    int findFirstIndex(Predicate<T> a, int from, int to);

    List<T> toList();

    PList<T> readOnly();

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
