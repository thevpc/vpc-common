package net.thevpc.common.util;

import java.util.*;

public interface MultiValueMap<K, V,L extends Collection<V>> extends Collection2 {
    void addValues(Map<K, V> map);

    <C extends Collection<V>> void addMultiValues(Map<K, C> map);

    void addMultiValues(MultiValueMap<K, V, L> map);

    abstract V getFirst(K a);

    boolean contains(K a, V value);

    void add(K k, V v);

    boolean remove(K a, V value);

    int keySize();

    int valueSize();

    L getValues(K a);

    boolean containsValue(V value);

    Set<Map.Entry<K, L>> multiValueEntrySet();

    List<Map.Entry<K, V>> valueEntryList();

    Iterable<Map.Entry<K, V>> valueEntries();

    Iterator<Map.Entry<K, V>> valueEntryIterator();

    Set<K> keySet();

    boolean containsKey(K key);
}
