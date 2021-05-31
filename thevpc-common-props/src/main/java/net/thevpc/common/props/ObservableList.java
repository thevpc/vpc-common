package net.thevpc.common.props;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ObservableList<T> extends ObservableValue<T>, Iterable<T> {

    /**
     * convenient method to return the first element or null
     * @return the first element or null
     */
    T get();

    T get(int index);

    int size();

    int[] findAllIndexes(Predicate<T> a);

    boolean contains(T a) ;
    
    java.util.List<T> findAll(Predicate<T> a);

    T findFirst(Predicate<T> a);

    T findFirst(Predicate<T> a, int from);

    T findFirst(Predicate<T> a, int from, int to);

    default int findFirstIndexOf(T a){
        return findFirstIndex(x-> Objects.equals(x,a));
    }

    int findFirstIndex(Predicate<T> a);

    int findFirstIndex(Predicate<T> a, int from);

    int findFirstIndex(Predicate<T> a, int from, int to);

    java.util.List<T> toList();

    ObservableList<T> readOnly();

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default T last() {
        return get(size() - 1);
    }

    default T first() {
        return get(0);
    }
}
