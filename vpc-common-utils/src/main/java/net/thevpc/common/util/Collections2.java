package net.thevpc.common.util;

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
     * a list of key-value pairs.
     * User adds values and the keyis automatically resolved by {@code keyResolver}
     *
     * @param keyResolver function to resolve key from value
     * @param <K>         Key type
     * @param <V>         value type
     * @return default implementation of KeyValueList<K, V>
     */
    public static <K, V> KeyValueList<K, V> keyValueList(Function<V, K> keyResolver) {
        return new DefaultKeyValueList<K, V>(keyResolver);
    }

    public static <K, V> KeyValueList<K, V> unmodifiableKeyValueList(KeyValueList<K, V> other) {
        return new UnmodifiableKeyValueList<>(other);
    }

    public static <K, V> AutoValueMap<K, V> autoValueMap(Function<K, V> autoValueSupplier) {
        return autoHashValueMap(autoValueSupplier);
    }

    public static <K, V> AutoValueMap<K, V> autoHashValueMap(Function<K, V> autoValueSupplier) {
        return new DefaultAutoValueMap<>(new HashMap<>(), autoValueSupplier);
    }

    public static <K, V> AutoValueMap<K, V> autoValueLinkedHashMap(Function<K, V> autoValueSupplier) {
        return new DefaultAutoValueMap<>(new LinkedHashMap<>(), autoValueSupplier);
    }

    public static <K, V> AutoValueMap<K, V> autoValueTreeMap(Function<K, V> autoValueSupplier) {
        return new DefaultAutoValueMap<>(new TreeMap<>(), autoValueSupplier);
    }
}
