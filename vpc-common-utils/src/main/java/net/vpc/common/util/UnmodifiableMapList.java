package net.vpc.common.util;

import java.util.Set;

/**
 * Created by vpc on 8/25/16.
 */
public class UnmodifiableMapList<K, V> extends UnmodifiableList<V>
        implements MapList<K, V> {

    final MapList<K, V> list;

    UnmodifiableMapList(MapList<K, V> list) {
        super(list);
        this.list = list;
    }

    @Override
    public V getByKey(K k) {
        return list.getByKey(k);
    }

    @Override
    public V removeByKey(K k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return list.containsKey(key);
    }

    @Override
    public boolean containsMappedValue(V value) {
        return list.containsMappedValue(value);
    }

    @Override
    public V getByValue(V value) {
        return list.getByValue(value);
    }

    @Override
    public boolean containsValue(Object value) {
        return list.containsValue(value);
    }

    @Override
    public Set<K> keySet() {
        return list.keySet();
    }
}