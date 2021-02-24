package net.thevpc.common.collections;

import java.util.*;
import java.util.function.Function;

public class Collections2 {

    public static <K, V> ListValueMap<K, V> listValueMap() {
        return arrayListValueHashMap();
    }

    public static <K, V> ListValueMap<K, V> arrayListValueHashMap() {
        return new DefaultListValueMap<>(new HashMap<>(), ArrayList::new);
    }

    public static <K, V> SetValueMap<K, V> setValueMap() {
        return hashSetValueHashMap();
    }

    public static <K, V> SetValueMap<K, V> hashSetValueHashMap() {
        return new DefaultSetValueMap<>(new HashMap<>(), HashSet::new);
    }

    /**
     * a list of key-value pairs. User adds values and the keys automatically
     * resolved by {@code keyResolver}
     *
     * @param keyResolver function to resolve key from value
     * @param <K> Key type
     * @param <V> value type
     * @return default implementation of KeyValueList&lt;K, V&gt;
     */
    public static <K, V> KeyValueList<K, V> keyValueList(Function<V, K> keyResolver) {
        return new DefaultKeyValueList<K, V>(keyResolver);
    }

    public static <K, V> KeyValueList<K, V> unmodifiableKeyValueList(KeyValueList<K, V> other) {
        return new UnmodifiableKeyValueList<>(other);
    }

    public static <K, V> AutoValueMap<K, V> autoValueMap(Function<K, V> autoValueSupplier) {
        return autoValueHashMap(autoValueSupplier);
    }

    public static <K, V> AutoValueMap<K, V> autoValueHashMap(Function<K, V> autoValueSupplier) {
        return new DefaultAutoValueMap<>(new HashMap<>(), autoValueSupplier);
    }

    public static <K, V> AutoValueMap<K, V> autoValueLinkedHashMap(Function<K, V> autoValueSupplier) {
        return new DefaultAutoValueMap<>(new LinkedHashMap<>(), autoValueSupplier);
    }

    public static <K, V> AutoValueMap<K, V> autoValueTreeMap(Function<K, V> autoValueSupplier) {
        return new DefaultAutoValueMap<>(new TreeMap<>(), autoValueSupplier);
    }

    public static EvictingCharQueue evictingCharQueue(int size) {
        return new EvictingCharQueue(size);
    }

    public static EvictingIntQueue evictingIntQueue(int size) {
        return new EvictingIntQueue(size);
    }

    public static <T> EvictingQueue<T> evictingQueue(int size) {
        return new EvictingQueue<>(size);
    }
}
