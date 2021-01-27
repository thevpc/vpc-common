package net.thevpc.common.swing.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/15/12
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class LRUMap<K,V> extends LinkedHashMap<K,V>{
    private int maxEntries;

    public LRUMap(int initialCapacity, float loadFactor, int maxEntries) {
        super(initialCapacity, loadFactor);
        this.maxEntries = maxEntries;
    }

    public LRUMap(int initialCapacity, int maxEntries) {
        super(initialCapacity);
        this.maxEntries = maxEntries;
    }

    public LRUMap(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public LRUMap(Map<? extends K, ? extends V> m, int maxEntries) {
        super(m);
        this.maxEntries = maxEntries;
    }

    public LRUMap(int initialCapacity, float loadFactor, boolean accessOrder, int maxEntries) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        return super.size() > maxEntries;
    }
}
