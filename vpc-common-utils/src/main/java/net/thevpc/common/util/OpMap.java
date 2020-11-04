package net.thevpc.common.util;

import java.util.*;

/**
 * Created by vpc on 3/12/17.
 */
public class OpMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private Map<K, V> adaptee;

    private OpMapValueFactory<K, V> factory;

    public OpMap(OpMapValueFactory<K, V> factory) {
        this((Map<K, V>) null, factory);
    }

    public OpMap(Map<K, V> adaptee, Class<V> factory) {
        this(adaptee, OpMap.<K, V>createOpMapValueFactory(factory));
    }

    public OpMap(Class<V> factory) {
        this((Map<K, V>) null, OpMap.<K, V>createOpMapValueFactory(factory));
    }

    public OpMap(Map<K, V> adaptee, OpMapValueFactory<K, V> factory) {
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

    public V plus(K key, V value) {
        V old = getOrCreate(key);
        V newVal = factory.add(old, value);
        adaptee.put(key, newVal);
        return newVal;
    }

    public V mul(K key, V value) {
        V old = getOrCreate(key);
        V newVal = factory.multiply(old, value);
        adaptee.put(key, newVal);
        return newVal;
    }

    public V div(K key, V value) {
        V old = getOrCreate(key);
        V newVal = factory.divide(old, value);
        adaptee.put(key, newVal);
        return newVal;
    }

    public V neg(K key) {
        V old = getOrCreate(key);
        V newVal = factory.neg((V) old);
        adaptee.put(key, newVal);
        return newVal;
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

    private static <K, V> OpMapValueFactory<K, V> createOpMapValueFactory(Class<V> cls) {
        switch (cls.getName()) {
            case "int":
            case "java.lang.Integer": {
                return new IntOpMapValueFactory();
            }
            case "long":
            case "java.lang.Long": {
                return new LongOpMapValueFactory();
            }
            case "float":
            case "java.lang.Float": {
                return new FloatOpMapValueFactory();
            }
            case "double":
            case "java.lang.Double": {
                return new DoubleOpMapValueFactory();
            }
        }
        throw new IllegalArgumentException("Unsupported yet");
    }

    private static class IntOpMapValueFactory<K> implements OpMapValueFactory<K, Integer> {
        @Override
        public Integer create(K key) {
            return null;
        }

        public Integer add(Integer oldValue, Integer newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.intValue() + newValue.intValue();
        }

        public Integer substract(Integer oldValue, Integer newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.intValue() + newValue.intValue();
        }

        public Integer multiply(Integer oldValue, Integer newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.intValue() * newValue.intValue();
        }

        public Integer divide(Integer oldValue, Integer newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.intValue() / newValue.intValue();
        }

        @Override
        public Integer neg(Integer oldValue) {
            if (oldValue == null) {
                return oldValue;
            }
            if (oldValue == null) {
                return oldValue;
            }
            return -oldValue.intValue();
        }
    }

    private static class LongOpMapValueFactory<K> implements OpMapValueFactory<K, Long> {
        @Override
        public Long create(K key) {
            return null;
        }

        public Long add(Long oldValue, Long newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.longValue() + newValue.longValue();
        }

        public Long substract(Long oldValue, Long newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.longValue() + newValue.longValue();
        }

        public Long multiply(Long oldValue, Long newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.longValue() * newValue.longValue();
        }

        public Long divide(Long oldValue, Long newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.longValue() / newValue.longValue();
        }

        @Override
        public Long neg(Long oldValue) {
            if (oldValue == null) {
                return oldValue;
            }
            if (oldValue == null) {
                return oldValue;
            }
            return -oldValue.longValue();
        }
    }

    private static class FloatOpMapValueFactory<K> implements OpMapValueFactory<K, Float> {
        @Override
        public Float create(K key) {
            return null;
        }

        public Float add(Float oldValue, Float newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.floatValue() + newValue.floatValue();
        }

        public Float substract(Float oldValue, Float newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.floatValue() + newValue.floatValue();
        }

        public Float multiply(Float oldValue, Float newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.floatValue() * newValue.floatValue();
        }

        public Float divide(Float oldValue, Float newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.floatValue() / newValue.floatValue();
        }

        @Override
        public Float neg(Float oldValue) {
            if (oldValue == null) {
                return oldValue;
            }
            if (oldValue == null) {
                return oldValue;
            }
            return -oldValue.floatValue();
        }
    }

    private static class DoubleOpMapValueFactory<K> implements OpMapValueFactory<K, Double> {
        @Override
        public Double create(K key) {
            return null;
        }

        public Double add(Double oldValue, Double newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.doubleValue() + newValue.doubleValue();
        }

        public Double substract(Double oldValue, Double newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.doubleValue() + newValue.doubleValue();
        }

        public Double multiply(Double oldValue, Double newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.doubleValue() * newValue.doubleValue();
        }

        public Double divide(Double oldValue, Double newValue) {
            if (oldValue == null) {
                return newValue;
            }
            if (newValue == null) {
                return newValue;
            }
            return oldValue.doubleValue() / newValue.doubleValue();
        }

        @Override
        public Double neg(Double oldValue) {
            if (oldValue == null) {
                return oldValue;
            }
            if (oldValue == null) {
                return oldValue;
            }
            return -oldValue.doubleValue();
        }
    }
}
