package net.vpc.common.util;

import java.util.*;

/**
 * Created by vpc on 6/2/16.
 */
public class SetValueMap<K, V> {
    private Map<K, Set<V>> data = new HashMap<>();
    private ValueFactory<V> valueFactory;

    public SetValueMap() {
        this.valueFactory = new DefaultValueFactory<V>();
    }

    public SetValueMap(ValueFactory<V> valueFactory) {
        this.valueFactory = valueFactory == null ? new DefaultValueFactory<V>() : valueFactory;
    }

    public boolean containsKey(K key){
        return data.containsKey(key);
    }

    public boolean containsValue(V value){
        for (Set<V> vs : data.values()) {
            if(vs.contains(value)){
                return true;
            }
        }
        return false;
    }

    public boolean containsKeyValue(K key,V value){
        Set<V> list = data.get(key);
        if (list != null) {
            return list.contains(value);
        }
        return false;
    }

    public Set<V> get(K key) {
        Set<V> list = data.get(key);
        if (list == null) {
            return list;
        }
        return Collections.unmodifiableSet(list);
    }

    public Set<K> keySet() {
        return data.keySet();
    }

    public void put(K key, V value) {
        Set<V> list = data.get(key);
        if (list == null) {
            list = valueFactory.create();
            data.put(key, list);
        }
        list.add(value);
    }

    public boolean remove(K key, V value) {
        Set<V> list = data.get(key);
        if (list != null) {
            boolean ok = list.remove(value);
            if (ok && list.size() == 0) {
                data.remove(key);
            }
            return ok;
        }
        return false;
    }

    public interface ValueFactory<V> {
        Set<V> create();
    }

    public static class DefaultValueFactory<V> implements ValueFactory<V> {
        @Override
        public Set<V> create() {
            return new HashSet<>();
        }
    }
}
