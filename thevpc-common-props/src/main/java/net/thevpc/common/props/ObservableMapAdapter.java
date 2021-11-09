/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 *
 * @author thevpc
 */
public abstract class ObservableMapAdapter<K, V> implements ObservableMap<K, V> {

    protected abstract ObservableMap<K, V> getObservableMapBase();

    @Override
    public V get(K key) {
        return getObservableMapBase().get(key);
    }

    @Override
    public int size() {
        return getObservableMapBase().size();
    }

    @Override
    public Map<K, V> toMap() {
        return getObservableMapBase().toMap();
    }

    @Override
    public boolean containsKey(K k) {
        return getObservableMapBase().containsKey(k);
    }

    @Override
    public ObservableList<K> keySet() {
        return getObservableMapBase().keySet();
    }

    @Override
    public ObservableList<V> values() {
        return getObservableMapBase().values();
    }

    @Override
    public ObservableList<MapEntry<K, V>> entrySet() {
        return getObservableMapBase().entrySet();
    }

    @Override
    public Set<MapEntry<K, V>> findAll(Predicate<MapEntry<K, V>> a) {
        return getObservableMapBase().findAll(a);
    }

    @Override
    public ObservableMap<K, V> readOnly() {
        return getObservableMapBase().readOnly();
    }

    @Override
    public String propertyName() {
        return getObservableMapBase().propertyName();
    }

    @Override
    public PropertyType propertyType() {
        return getObservableMapBase().propertyType();
    }

    @Override
    public boolean isWritable() {
        return getObservableMapBase().isWritable();
    }

    @Override
    public UserObjects userObjects() {
        return getObservableMapBase().userObjects();
    }

    @Override
    public PropertyListeners events() {
        return getObservableMapBase().events();
    }

    @Override
    public Iterator<MapEntry<K, V>> iterator() {
        return getObservableMapBase().iterator();
    }

}
