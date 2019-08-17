package net.vpc.common.util;

/**
 * Created by vpc on 3/12/17.
 * @param <K>
 * @param <V>
 */
public interface OpMapValueFactory<K,V> {
    V create(K key);
    V neg(V oldValue);
    V add(V oldValue, V newValue);
    V substract(V oldValue, V newValue);
    V multiply(V oldValue, V newValue);
    V divide(V oldValue, V newValue);
}
