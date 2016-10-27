package net.vpc.common.util;

/**
 * Created by vpc on 6/25/16.
 */
public interface Converter<K, V> {
    K convert(V value);
}
