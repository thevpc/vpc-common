package net.thevpc.common.collections;

import java.util.*;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public abstract class AbstractMultiValueMap<K, V, L extends Collection<V>> implements MultiValueMap<K, V, L> {
    private Map<K, L> map;

    public AbstractMultiValueMap() {
    }

    protected void initMap(Map<K, L> map) {
        if (map == null) {
            throw new IllegalArgumentException("Map is Null");
        }
        if (!map.isEmpty()) {
            throw new IllegalArgumentException("Map should not be empty");
        }
        this.map = map;
    }

    @Override
    public void addValues(Map<K, V> map) {
        for (Map.Entry<K, V> kvEntry : map.entrySet()) {
            add(kvEntry.getKey(), kvEntry.getValue());
        }
    }

    @Override
    public <C extends Collection<V>> void addMultiValues(Map<K, C> map) {
        for (Map.Entry<K, C> klEntry : map.entrySet()) {
            for (V v : klEntry.getValue()) {
                add(klEntry.getKey(), v);
            }
        }
    }

    @Override
    public void addMultiValues(MultiValueMap<K, V, L> map) {
        for (Map.Entry<K, L> klEntry : map.multiValueEntrySet()) {
            for (V v : klEntry.getValue()) {
                add(klEntry.getKey(), v);
            }
        }
    }

    @Override
    public V getFirst(K a) {
        L all = map.get(a);
        if (all == null) {
            return null;
        }
        if (all.size() > 0) {
            return resolveFirst(all);
        }
        return null;
    }

    @Override
    public boolean contains(K a, V value) {
        L all = map.get(a);
        if (all != null) {
            return all.contains(value);
        }
        return false;
    }

    @Override
    public void add(K k, V v) {
        get0(k).add(v);
    }

    private L get0(K k) {
        L list = map.get(k);
        if (list == null) {
            list = createCollection();
            map.put(k, list);
        }
        return list;
    }

    //    @Override
//    public boolean remove(K a, V value) {
//        L all = map.get(a);
//        if (all != null) {
//            return all.remove(value);
//        }
//        return false;
//    }
    public boolean remove(K key, V value) {
        L list = map.get(key);
        if (list != null) {
            boolean ok = list.remove(value);
            if (ok && list.size() == 0) {
                map.remove(key);
            }
            return ok;
        }
        return false;
    }

    @Override
    public int keySize() {
        return map.size();
    }

    @Override
    public int valueSize() {
        int count = 0;
        for (Map.Entry<K, L> entry : map.entrySet()) {
            count += entry.getValue().size();
        }
        return count;
    }

    @Override
    public L getValues(K a) {
        L all = map.get(a);
        if (all == null) {
            return emptyCollection();
        }
        if (all.size() > 0) {
            return unmodifiableCollection(all);
        } else {
            return emptyCollection();
        }
    }

    @Override
    public boolean containsValue(V value) {
        for (L vs : map.values()) {
            if (vs.contains(value)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Set<Map.Entry<K, L>> multiValueEntrySet() {
        return map.entrySet();
    }

    @Override
    public List<Map.Entry<K, V>> valueEntryList() {
        ArrayList<Map.Entry<K, V>> kv = new ArrayList<>();
        for (Map.Entry<K, L> klEntry : map.entrySet()) {
            K k = klEntry.getKey();
            for (V v : klEntry.getValue()) {
                kv.add(new AbstractMap.SimpleEntry<K, V>(k, v));
            }
        }
        return kv;
    }

    @Override
    public Iterable<Map.Entry<K, V>> valueEntries() {
        return new ValueEntriesIterable();
    }

    @Override
    public Iterator<Map.Entry<K, V>> valueEntryIterator() {
        return new ValueEntryIterator();
    }

    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsKeyValue(K key, V value) {
        L list = map.get(key);
        if (list != null) {
            return list.contains(value);
        }
        return false;
    }

    protected abstract L createCollection();

    protected abstract L unmodifiableCollection(L l);

    protected abstract L emptyCollection();

    protected V resolveFirst(L all) {
        if (all instanceof List) {
            return ((List<V>) all).get(0);
        }
        Iterator<V> it = all.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    private class ValueEntryIterator implements Iterator<Map.Entry<K, V>> {
        Iterator<Map.Entry<K, L>> it1 = map.entrySet().iterator();
        Iterator<V> it2;
        K currentKey;
        boolean finished = false;

        @Override
        public boolean hasNext() {
            if (finished) {
                return false;
            }
            if (it2 == null) {
                if (it1.hasNext()) {
                    Map.Entry<K, L> it1Next = it1.next();
                    currentKey = it1Next.getKey();
                    it2 = it1Next.getValue().iterator();
                } else {
                    finished = true;
                    return false;
                }
            }
            boolean b = it2.hasNext();
            if (!b) {
                it2 = null;
            }
            return b;
        }

        @Override
        public Map.Entry<K, V> next() {
            return new AbstractMap.SimpleEntry<>(currentKey, it2.next());
        }
    }

    private class ValueEntriesIterable implements Iterable<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return valueEntryIterator();
        }
    }
}
