package net.vpc.common.util;

import java.util.*;

/**
 * Created by vpc on 3/12/17.
 */
public class VMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private Map<K, V> adaptee;
    private VMapValueFactory<K, V> factory;

    public VMap(VMapValueFactory<K, V> factory) {
        this(null, factory);
    }

    public VMap(Map<K, V> adaptee, VMapValueFactory<K, V> factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        this.adaptee = adaptee == null ? new HashMap<K, V>() : adaptee;
        this.factory = factory;
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

    public V getOrCreate(K key) {
        if (adaptee.containsKey(key)) {
            return adaptee.get(key);
        } else {
            V v = factory.create(key);
            adaptee.put(key, v);
            return v;
        }
    }

    public V plus(K key, int value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() + value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() + value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() + value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() + value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() + value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() + value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V mul(K key, int value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() * value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() * value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() * value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() * value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() * value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() * value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V div(K key, int value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() / value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() / value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() / value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() / value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() / value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() / value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V neg(K key) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = -old.intValue();
        } else if (Long.class.isInstance(old)) {
            newVal = -old.longValue();
        } else if (Float.class.isInstance(old)) {
            newVal = -old.floatValue();
        } else if (Double.class.isInstance(old)) {
            newVal = -old.doubleValue();
        } else if (Byte.class.isInstance(old)) {
            newVal = -old.byteValue();
        } else if (Short.class.isInstance(old)) {
            newVal = -old.shortValue();
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V plus(K key, double value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() + value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() + value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() + value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() + value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() + value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() + value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V mul(K key, double value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() * value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() * value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() * value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() * value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() * value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() * value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V div(K key, double value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() / value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() / value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() / value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() / value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() / value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() / value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V plus(K key, long value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() + value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() + value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() + value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() + value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() + value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() + value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V mul(K key, long value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() * value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() * value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() * value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() * value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() * value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() * value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V div(K key, long value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() / value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() / value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() / value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() / value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() / value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() / value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V plus(K key, float value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() + value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() + value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() + value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() + value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() + value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() + value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V mul(K key, float value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() * value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() * value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() * value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() * value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() * value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() * value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
    }

    public V div(K key, float value) {
        Number old = (Number) getOrCreate(key);
        Number newVal = old;
        if (old == null) {
            // do nothing
        } else if (Integer.class.isInstance(old)) {
            newVal = old.intValue() / value;
        } else if (Long.class.isInstance(old)) {
            newVal = old.longValue() / value;
        } else if (Float.class.isInstance(old)) {
            newVal = old.floatValue() / value;
        } else if (Double.class.isInstance(old)) {
            newVal = old.doubleValue() / value;
        } else if (Byte.class.isInstance(old)) {
            newVal = old.byteValue() / value;
        } else if (Short.class.isInstance(old)) {
            newVal = old.shortValue() / value;
        } else {
            throw new ClassCastException();
        }
        adaptee.put(key, (V) newVal);
        return (V) newVal;
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
