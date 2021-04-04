package net.thevpc.common.props.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.MapEntry;
import net.thevpc.common.props.ObservableList;
import net.thevpc.common.props.ObservableMap;

public class ReadOnlyMap<K, V> extends DelegateProperty implements ObservableMap<K, V> {

    public ReadOnlyMap(ObservableMap<K, V> base) {
        super(base);
    }

    @Override
    public Map<K, V> toMap() {
        return getBase().toMap();
    }

    @Override
    public boolean containsKey(K k) {
        return getBase().containsKey(k);
    }

    @Override
    public ObservableMap<K, V> getBase() {
        return (ObservableMap<K, V>) super.getBase();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public ObservableMap<K, V> readOnly() {
        return this;
    }

    @Override
    public V get(K key) {
        return getBase().get(key);
    }

    @Override
    public int size() {
        return getBase().size();
    }

    @Override
    public ObservableList<K> keySet() {
        ObservableList<K> ks = getBase().keySet();
        if (ks instanceof WritableList) {
            return ((WritableList<K>) ks).readOnly();
        }
        return ks;
    }

    @Override
    public ObservableList<V> values() {
        ObservableList<V> ks = getBase().values();
        if (ks instanceof WritableList) {
            return ((WritableList<V>) ks).readOnly();
        }
        return ks;
    }

    @Override
    public ObservableList<MapEntry<K, V>> entrySet() {
        ObservableList<MapEntry<K, V>> ks = getBase().entrySet();
        if (ks instanceof WritableList) {
            return ((WritableList<MapEntry<K, V>>) ks).readOnly();
        }
        return ks;
    }

    @Override
    public Set<MapEntry<K, V>> findAll(Predicate<MapEntry<K, V>> a) {
        return getBase().findAll(a);
    }

    @Override
    public Iterator<MapEntry<K, V>> iterator() {
        return entrySet().iterator();
    }
}
