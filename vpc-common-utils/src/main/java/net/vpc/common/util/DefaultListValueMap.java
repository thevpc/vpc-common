package net.vpc.common.util;

import java.util.*;
import java.util.function.Supplier;

/**
 * A map that holds a list of values for each key
 * Created by vpc on 6/2/16.
 */
public class DefaultListValueMap<K, V> extends AbstractMultiValueMap<K, V, List<V>> implements ListValueMap<K,V> {
    private Supplier<List<V>> collectionFactory;
    public DefaultListValueMap(Map<K, List<V>> map, Supplier<List<V>> collectionFactory) {
        if(map==null){
            map=new HashMap<>();
        }
        if(collectionFactory ==null){
            collectionFactory = ArrayList::new;
        }
        this.collectionFactory = collectionFactory;
        initMap(map);
    }

    protected List<V> createCollection() {
        return collectionFactory.get();
    }

    protected List<V> unmodifiableCollection(List<V> l) {
        return Collections.unmodifiableList(l);
    }

    protected List<V> emptyCollection() {
        return Collections.emptyList();
    }

    protected V resolveFirst(List<V> all) {
        return all.get(0);
    }
}
