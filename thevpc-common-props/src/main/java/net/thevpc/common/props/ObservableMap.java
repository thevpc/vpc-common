package net.thevpc.common.props;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ObservableMap<K, V> extends Property, Iterable<MapEntry<K, V>> {

    V get(K key);

    int size();

    java.util.Map<K, V> toMap();

    boolean containsKey(K k);
    
    ObservableList<K> keySet();

    ObservableList<V> values();

    ObservableList<MapEntry<K, V>> entrySet();

    Set<MapEntry<K, V>> findAll(Predicate<MapEntry<K, V>> a);

    ObservableMap<K, V> readOnly();

    default Stream<MapEntry<K, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}
