package net.thevpc.common.util;

import java.util.Set;

/**
 * Created by vpc on 8/25/16.
 */
public class UnmodifiableKeyValueList<K, V> extends UnmodifiableList<V>
        implements KeyValueList<K, V> {

    final KeyValueList<K, V> list;

    UnmodifiableKeyValueList(KeyValueList<K, V> list) {
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