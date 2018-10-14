package net.vpc.common.util;

import java.util.*;

/**
 * A map that holds a list of values for each key
 * Created by vpc on 6/2/16.
 */
public class ListValueMap<K, V> {
    private Map<K, List<V>> data = new HashMap<K, List<V>>();
    private ValueFactory<V> valueFactory;

    public ListValueMap() {
        this.valueFactory = new DefaultValueFactory<V>();
    }

    public ListValueMap(ValueFactory<V> valueFactory) {
        this.valueFactory = valueFactory == null ? new DefaultValueFactory<V>() : valueFactory;
    }

    public List<V> get(K key) {
        List<V> list = data.get(key);
        if (list == null) {
            return list;
        }
        return Collections.unmodifiableList(list);
    }

    public boolean containsKey(K key){
        return data.containsKey(key);
    }

    public boolean containsValue(V value){
        for (List<V> vs : data.values()) {
            if(vs.contains(value)){
                return true;
            }
        }
        return false;
    }

    public boolean containsKeyValue(K key,V value){
        List<V> list = data.get(key);
        if (list != null) {
            return list.contains(value);
        }
        return false;
    }

    public Set<K> keySet() {
        return data.keySet();
    }

    public void put(K key, V value) {
        List<V> list = data.get(key);
        if (list == null) {
            list = new ArrayList<V>();
            data.put(key, list);
        }
        list.add(value);
    }

    public boolean remove(K key, V value) {
        List<V> list = data.get(key);
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
        List<V> create();
    }

    public static class DefaultValueFactory<V> implements ValueFactory<V> {
        //@Override
        public List<V> create() {
            return new ArrayList<V>();
        }
    }

}
