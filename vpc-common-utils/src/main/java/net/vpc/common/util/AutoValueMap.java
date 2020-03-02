package net.vpc.common.util;

import java.util.Map;

public interface AutoValueMap<K, V> extends Map<K, V>,Collection2 {
    V getOrCreate(K key);
}
