package net.vpc.common.prpbind;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface PMap<K, V> extends Property, Iterable<PMapEntry<K, V>> {

    V get(K key);

    int size();

    PList<K> keySet();

    PList<V> values();

    PList<PMapEntry<K, V>> entrySet();

    Set<PMapEntry<K, V>> findAll(Predicate<PMapEntry<K, V>> a);

    default Stream<PMapEntry<K, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}
