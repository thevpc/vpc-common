package net.vpc.common.util;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by vpc on 6/2/16.
 */
public class DefaultSetValueMap<K, V> extends AbstractMultiValueMap<K, V, Set<V>> implements SetValueMap<K,V> {
    private Supplier<Set<V>> collectionFactory;

    public DefaultSetValueMap(Map<K, Set<V>> map, Supplier<Set<V>> collectionFactory) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (collectionFactory == null) {
            collectionFactory = HashSet::new;
        }
        this.collectionFactory = collectionFactory;
        initMap(map);
    }

    protected Set<V> createCollection() {
        return collectionFactory.get();
    }

    protected Set<V> unmodifiableCollection(Set<V> l) {
        return Collections.unmodifiableSet(l);
    }

    protected Set<V> emptyCollection() {
        return Collections.emptySet();
    }

    protected V resolveFirst(Set<V> all) {
        Iterator<V> it = all.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }
}
