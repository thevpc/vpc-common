package net.thevpc.common.collections;

import java.util.Map;

public interface AutoValueMap<K, V> extends Map<K, V>,Collection2 {
    V getOrCreate(K key);
}
