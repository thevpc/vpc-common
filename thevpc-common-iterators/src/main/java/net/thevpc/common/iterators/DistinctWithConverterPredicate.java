package net.thevpc.common.iterators;


import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

class DistinctWithConverterPredicate<F, T> implements Predicate<F> {
    private final Function<F, T> converter;
    HashSet<T> visited;

    public DistinctWithConverterPredicate(Function<F, T> converter) {
        this.converter = converter;
        visited = new HashSet<>();
    }

    @Override
    public boolean test(F value) {
        T t = converter.apply(value);
        if (visited.contains(t)) {
            return false;
        }
        visited.add(t);
        return true;
    }

    @Override
    public String toString() {
        return "DistinctConverter";
    }
}
