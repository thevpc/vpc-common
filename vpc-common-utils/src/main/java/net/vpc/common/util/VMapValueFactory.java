package net.vpc.common.util;

/**
 * Created by vpc on 3/12/17.
 */
public interface VMapValueFactory<K,V> {
    V create(K key);
}
