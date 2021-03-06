package net.thevpc.common.collections;

import java.util.*;
import java.util.function.Function;

/**
 * Created by vpc on 3/12/17.
 */
public class DefaultAutoValueMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, AutoValueMap<K, V> {
    private Map<K, V> adaptee;

    private Function<K, V> autoValueSupplier;

    public DefaultAutoValueMap(Function<K, V> autoValueSupplier) {
        this((Map<K, V>) null, autoValueSupplier);
    }

//    public VMap(Map<K, V> adaptee, Class<V> factory) {
//        this(adaptee, VMap.<K, V>createVMapValueFactory(factory));
//    }
//
//    public VMap(Class<V> factory) {
//        this((Map<K, V>) null, VMap.<K, V>createVMapValueFactory(factory));
//    }
//
    public DefaultAutoValueMap(Map<K, V> adaptee, Function<K, V> autoValueSupplier) {
        if (autoValueSupplier == null) {
            throw new NullPointerException();
        }
        this.adaptee = adaptee == null ? new HashMap<K, V>() : adaptee;
        this.autoValueSupplier = autoValueSupplier;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return adaptee.entrySet();
    }

    @Override
    public int size() {
        return adaptee.size();
    }

    @Override
    public boolean isEmpty() {
        return adaptee.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return adaptee.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return adaptee.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return adaptee.get(key);
    }

    @Override
    public V getOrCreate(K key) {
        if (adaptee.containsKey(key)) {
            return adaptee.get(key);
        } else {
            V v = autoValueSupplier.apply(key);
            adaptee.put(key, v);
            return v;
        }
    }

    @Override
    public V put(K key, V value) {
        return adaptee.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return adaptee.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        adaptee.putAll(m);
    }

    @Override
    public void clear() {
        adaptee.clear();
    }

    @Override
    public Set<K> keySet() {
        return adaptee.keySet();
    }

    @Override
    public Collection<V> values() {
        return adaptee.values();
    }

    @Override
    public boolean equals(Object o) {
        return adaptee.equals(o);
    }

    @Override
    public int hashCode() {
        return adaptee.hashCode();
    }

    @Override
    public String toString() {
        return adaptee.toString();
    }




}
