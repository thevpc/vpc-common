package net.vpc.common.props.impl;


import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.vpc.common.props.*;

public abstract class AbstractWritablePLMapImpl<K, V> extends AbstractProperty implements WritablePLMap<K, V> {

    private PMap<K, V> ro;
    private Function<V, K> valueToKey;
    private WritablePList<K> keySet;
    private WritablePList<PMapEntry<K, V>> entrySet;
    private WritablePList<V> valueList;

    public AbstractWritablePLMapImpl(String name, PropertyType keyType, PropertyType valueType, Function<V, K> valueToKey) {
        super(name, PropertyType.of(Map.class, keyType, valueType));
        this.valueToKey = valueToKey;
        keySet = new KeySetListImpl(name + "#keySet", keyType);
        valueList = new ValueListListImpl(name + "#valueList", keyType);
        entrySet = new EntrySetListImpl(name + "#entrySet", valueType);
    }

    @Override
    public Map<K, V> toMap() {
        LinkedHashMap<K, V> m = new LinkedHashMap<>();
        for (PMapEntry<K, V> e : this) {
            m.put(e.getKey(), e.getValue());
        }
        return m;
    }

    protected abstract Set<Map.Entry<K, V>> entrySetImpl();

    @Override
    public void removeAll() {
        for (Map.Entry<K, V> k : new HashSet<Map.Entry<K, V>>(entrySetImpl())) {
            remove(k.getKey());
        }
    }

    @Override
    public void removeAll(Predicate<PMapEntry<K, V>> a) {
        for (Map.Entry<K, V> k : new HashSet<Map.Entry<K, V>>(entrySetImpl())) {
            PMapEntry<K, V> kv = new PMapEntryImpl<K, V>(k.getKey(), k.getValue());
            if (a.test(kv)) {
                remove(k.getKey());
            }
        }
    }

    @Override
    public V add(V v) {
        K k = valueToKey.apply(v);
        return put(k, v);
    }

    private V put(K k, V v) {
        if (!containsKeyImpl(k)) {
            V v0 = putImpl(k, v);
            if (v0 instanceof WithListeners) {
                listeners.removeDelegate((WithListeners) v0);
            }
            if (v instanceof WithListeners) {
                listeners.addDelegate((WithListeners) v, () -> "/" + k);
            }
//            if(k instanceof WithListeners){
//                listeners.addDelegate((WithListeners) k, () -> "/");
//            }
            listeners.firePropertyUpdated(new PropertyEvent(
                    this,
                    k,
                    v0,
                    v,
                    "/",
                    PropertyUpdate.UPDATE
            ));
            return v0;
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                k,
                null,
                v,
                "/",
                PropertyUpdate.ADD
        ));
        return null;
    }

    @Override
    public V remove(K k) {
        if (containsKeyImpl(k)) {
            V v0 = removeImpl(k);
            if (v0 instanceof WithListeners) {
                listeners.removeDelegate((WithListeners) v0);
            }
            listeners.firePropertyUpdated(new PropertyEvent(
                    this,
                    k,
                    v0,
                    null,
                    "/",
                    PropertyUpdate.REMOVE
            ));
            return v0;
        }
        return null;
    }

    @Override
    public WritablePList<V> values() {
        return valueList;
    }

    @Override
    public PMap<K, V> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyPMap<K, V>(this);
        }
        return ro;
    }

    protected abstract boolean containsKeyImpl(K k);

    protected abstract int sizeImpl();

    protected abstract V getImpl(K k);

    protected abstract V putImpl(K k, V v);

    protected abstract V removeImpl(K k);

    @Override
    public V get(K index) {
        return getImpl(index);
    }

    @Override
    public int size() {
        return sizeImpl();
    }

    @Override
    public WritablePList<K> keySet() {
        return keySet;
    }

    @Override
    public WritablePList<PMapEntry<K, V>> entrySet() {
        return entrySet;
    }

    @Override
    public Set<PMapEntry<K, V>> findAll(Predicate<PMapEntry<K, V>> a) {
        return entrySet().stream().filter(a).collect(Collectors.toSet());
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    public Iterator<PMapEntry<K, V>> iterator() {
        return entrySet().iterator();
    }

    private class KeySetListImpl extends AbstractWritablePListImpl<K> {

        public KeySetListImpl(String name, PropertyType elementType) {
            super(name, elementType);
        }

        @Override
        protected void addImpl(int index, K v) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected K setImpl(int index, K v) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected int indexOfImpl(K v) {
            int index = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (Objects.equals(e.getKey(), v)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @Override
        protected K removeImpl(int index) {
            int index0 = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (index0 == index) {
                    AbstractWritablePLMapImpl.this.remove(e.getKey());
                    return e.getKey();
                }
                index0++;
            }
            return null;
        }

        @Override
        protected K getImpl(int index) {
            int index0 = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (index0 == index) {
                    AbstractWritablePLMapImpl.this.remove(e.getKey());
                    return e.getKey();
                }
                index0++;
            }
            return null;
        }

        @Override
        protected int sizeImpl() {
            return AbstractWritablePLMapImpl.this.size();
        }
    }

    private class EntrySetListImpl extends AbstractWritablePListImpl<PMapEntry<K, V>> {

        public EntrySetListImpl(String name, PropertyType elementType) {
            super(name, elementType);
        }

        @Override
        protected void addImpl(int index, PMapEntry<K, V> e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected PMapEntry<K, V> setImpl(int index, PMapEntry<K, V> e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected int indexOfImpl(PMapEntry<K, V> item) {
            int index = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (Objects.equals(e, item)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @Override
        protected PMapEntry<K, V> removeImpl(int index) {
            int index0 = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (index0 == index) {
                    AbstractWritablePLMapImpl.this.remove(e.getKey());
                    return e;
                }
                index0++;
            }
            return null;
        }

        @Override
        protected PMapEntry<K, V> getImpl(int index) {
            int index0 = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (index0 == index) {
                    AbstractWritablePLMapImpl.this.remove(e.getKey());
                    return e;
                }
                index0++;
            }
            return null;
        }

        @Override
        protected int sizeImpl() {
            return AbstractWritablePLMapImpl.this.size();
        }

        @Override
        public Iterator<PMapEntry<K, V>> iterator() {
            Iterator<Map.Entry<K, V>> it = entrySetImpl().iterator();
            return new Iterator<PMapEntry<K, V>>() {
                Map.Entry<K, V> curr = null;

                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public PMapEntry<K, V> next() {
                    curr = it.next();
                    return new PMapEntryImpl<>(curr.getKey(), curr.getValue());
                }

                @Override
                public void remove() {
                    AbstractWritablePLMapImpl.this.remove(curr.getKey());
                }
            };
        }
    }

    private class ValueListListImpl extends AbstractWritablePListImpl<V> {

        public ValueListListImpl(String name, PropertyType elementType) {
            super(name, elementType);
        }

        @Override
        protected void addImpl(int index, V e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected V setImpl(int index, V e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected int indexOfImpl(V item) {
            int index = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (Objects.equals(e.getValue(), item)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @Override
        protected V removeImpl(int index) {
            int index0 = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (index0 == index) {
                    V v = AbstractWritablePLMapImpl.this.remove(e.getKey());
                    return v;
                }
                index0++;
            }
            return null;
        }

        @Override
        protected V getImpl(int index) {
            int index0 = 0;
            for (PMapEntry<K, V> e : AbstractWritablePLMapImpl.this) {
                if (index0 == index) {
                    return AbstractWritablePLMapImpl.this.get(e.getKey());
                }
                index0++;
            }
            return null;
        }

        @Override
        protected int sizeImpl() {
            return AbstractWritablePLMapImpl.this.size();
        }
    }

    @Override
    public String toString() {
        return "WritablePMap{"
                + "name='" + name() + '\''
                + ", type=" + type()
                + " value=[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + '}';
    }

}
