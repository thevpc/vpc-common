package net.vpc.common.util;

import java.util.*;

public class MapUtils {

    public static <K, V> Map<K, V> linkedmap(Entry<K, V>... values) {
        return map(new LinkedHashMap<K,V>(values == null ? 1 : values.length),(Entry<K, V>[]) values);
    }

    public static <K, V> Map<K, V> linkedmap(Object... values) {
        return map(new LinkedHashMap<K,V>(values == null ? 1 : values.length), values);
    }

    public static <K, V> Map<K, V> map(Entry<K, V>... values) {
        return map(new HashMap<K,V>(values == null ? 1 : values.length), (Entry<K, V>[]) values);
    }

    public static <K, V> Map<K, V> map(Object... values) {
        return map(new HashMap<K,V>(values == null ? 1 : values.length), (Object[]) values);
    }

    private static <K, V> Entry[] entries(Object... values) {
        if(Entry.class.isAssignableFrom(values.getClass().getComponentType())){
            return (Entry<K, V>[]) values;
        }
        List<Entry<K, V>> list = new ArrayList<>();
        for (int i = 0; i < values.length; i += 2) {
            list.add(new Entry<K, V>((K) values[i], (V) values[i + 1]));
        }
        return list.toArray(new Entry[0]);
    }

    public static <K, V> Map<K, V> map(Map<K, V> m, Object... values) {
        return map(m, (Entry<K, V>[]) entries(values));
    }

    public static <K, V> Map<K, V> map(Map<K, V> m, Entry<K, V>... values) {
        if (m == null) {
            m = new HashMap<>(values == null ? 1 : values.length);
        }
        if (values != null) {
            for (Entry<K, V> value : values) {
                if (value != null) {
                    m.put(value.key, value.value);
                }
            }
        }
        return m;
    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
