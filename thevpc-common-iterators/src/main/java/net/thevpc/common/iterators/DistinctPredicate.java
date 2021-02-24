package net.thevpc.common.iterators;

import java.util.HashSet;
import java.util.function.Predicate;

class DistinctPredicate<T> implements Predicate<T> {
    private HashSet<T> visited = new HashSet<>();

    @Override
    public boolean test(T value) {
        if (visited.contains(value)) {
            return false;
        }
        visited.add(value);
        return true;
    }

    @Override
    public String toString() {
        return "distinct";
    }
}
