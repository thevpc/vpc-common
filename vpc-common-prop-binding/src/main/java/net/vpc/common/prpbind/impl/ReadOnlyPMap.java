package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.PList;
import net.vpc.common.prpbind.PMap;
import net.vpc.common.prpbind.PMapEntry;
import net.vpc.common.prpbind.WritablePList;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class ReadOnlyPMap<K, V> extends DelegateProperty implements PMap<K, V> {
    public ReadOnlyPMap(PMap<K, V> base) {
        super(base);
    }

    @Override
    public PMap<K, V> getBase() {
        return (PMap<K, V>) super.getBase();
    }

    @Override
    public boolean isRefWritable() {
        return false;
    }

    @Override
    public boolean isValueWritable() {
        return false;
    }

    @Override
    public V get(K key) {
        return getBase().get(key);
    }

    @Override
    public int size() {
        return getBase().size();
    }

    @Override
    public PList<K> keySet() {
        PList<K> ks = getBase().keySet();
        if (ks instanceof WritablePList) {
            return ((WritablePList<K>) ks).readOnly();
        }
        return ks;
    }

    @Override
    public PList<V> values() {
        PList<V> ks = getBase().values();
        if (ks instanceof WritablePList) {
            return ((WritablePList<V>) ks).readOnly();
        }
        return ks;
    }

    @Override
    public PList<PMapEntry<K, V>> entrySet() {
        PList<PMapEntry<K, V>> ks = getBase().entrySet();
        if (ks instanceof WritablePList) {
            return ((WritablePList<PMapEntry<K, V>>) ks).readOnly();
        }
        return ks;
    }

    @Override
    public Set<PMapEntry<K, V>> findAll(Predicate<PMapEntry<K, V>> a) {
        return getBase().findAll(a);
    }

    @Override
    public Iterator<PMapEntry<K, V>> iterator() {
        return entrySet().iterator();
    }
}
