package net.thevpc.common.props.impl;


import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.thevpc.common.props.*;

public abstract class AbstractWritableMapImpl<K, V> extends WritablePropertyBase implements WritableMap<K, V> {

    private ObservableMap<K, V> ro;
    private WritableList<K> keySet;
    private WritableList<MapEntry<K, V>> entrySet;
    private WritableList<V> valueList;

    public AbstractWritableMapImpl(String name, PropertyType keyType, PropertyType valueType) {
        super(name, PropertyType.of(Map.class, keyType, valueType));
        keySet = new KeySetListImpl(name + "#keySet", keyType);
        valueList = new ValueListListImpl(name + "#valueList", keyType);
        entrySet = new EntrySetListImpl(name + "#entrySet", valueType);
    }

    @Override
    public boolean containsKey(K k) {
        return keySet.contains(k);
    }

    
    @Override
    public Map<K, V> toMap() {
        LinkedHashMap<K, V> m = new LinkedHashMap<>();
        for (MapEntry<K, V> e : this) {
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
    public void removeAll(Predicate<MapEntry<K, V>> a) {
        for (Map.Entry<K, V> k : new HashSet<Map.Entry<K, V>>(entrySetImpl())) {
            MapEntry<K, V> kv = new MapEntryImpl<K, V>(k.getKey(), k.getValue());
            if (a.test(kv)) {
                remove(k.getKey());
            }
        }
    }

    @Override
    public V put(K k, V v) {
        if (containsKeyImpl(k)) {
            V v0 = putImpl(k, v);
            if (v0 instanceof Property) {
                listeners.removeDelegate((Property) v0);
            }
            if (v instanceof Property) {
                listeners.addDelegate((Property) v, () ->
                                Path.root().append(String.valueOf(k))
                        );
            }
//            if(k instanceof WithListeners){
//                events.addDelegate((WithListeners) k, () -> "/");
//            }
            ((DefaultPropertyListeners)listeners).firePropertyUpdated(new PropertyEvent(
                    this,
                    k,
                    v0,
                    v,
                    Path.root(),
                    PropertyUpdate.UPDATE,true
            ));
            return v0;
        }
        V v0 = putImpl(k, v);
        ((DefaultPropertyListeners)listeners).firePropertyUpdated(new PropertyEvent(
                this,
                k,
                null,
                v,
                Path.root(),
                PropertyUpdate.ADD,true
        ));
        return null;
    }

    @Override
    public V remove(K k) {
        if (containsKeyImpl(k)) {
            V v0 = removeImpl(k);
            if (v0 instanceof Property) {
                listeners.removeDelegate((Property) v0);
            }
            ((DefaultPropertyListeners)listeners).firePropertyUpdated(new PropertyEvent(
                    this,
                    k,
                    v0,
                    null,
                    Path.root(),
                    PropertyUpdate.REMOVE,true
            ));
            return v0;
        }
        return null;
    }

    @Override
    public WritableList<V> values() {
        return valueList;
    }

    @Override
    public ObservableMap<K, V> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyMap<K, V>(this);
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
    public WritableList<K> keySet() {
        return keySet;
    }

    @Override
    public WritableList<MapEntry<K, V>> entrySet() {
        return entrySet;
    }

    @Override
    public Set<MapEntry<K, V>> findAll(Predicate<MapEntry<K, V>> a) {
        return entrySet().stream().filter(a).collect(Collectors.toSet());
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    public Iterator<MapEntry<K, V>> iterator() {
        return entrySet().iterator();
    }

    private class KeySetListImpl extends WritableListBase<K> {

        public KeySetListImpl(String name, PropertyType elementType) {
            super(name, elementType);
        }

        @Override
        protected boolean addImpl(int index, K v) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected K setImpl(int index, K v) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected int indexOfImpl(K v) {
            int index = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (Objects.equals(e.getKey(), v)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @Override
        protected K removeAtImpl(int index) {
            int index0 = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (index0 == index) {
                    AbstractWritableMapImpl.this.remove(e.getKey());
                    return e.getKey();
                }
                index0++;
            }
            return null;
        }

        @Override
        protected K getImpl(int index) {
            int index0 = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (index0 == index) {
                    AbstractWritableMapImpl.this.remove(e.getKey());
                    return e.getKey();
                }
                index0++;
            }
            return null;
        }

        @Override
        protected int sizeImpl() {
            return AbstractWritableMapImpl.this.size();
        }
    }

    private class EntrySetListImpl extends WritableListBase<MapEntry<K, V>> {

        public EntrySetListImpl(String name, PropertyType elementType) {
            super(name, elementType);
        }

        @Override
        protected boolean addImpl(int index, MapEntry<K, V> e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected MapEntry<K, V> setImpl(int index, MapEntry<K, V> e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected int indexOfImpl(MapEntry<K, V> item) {
            int index = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (Objects.equals(e, item)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @Override
        protected MapEntry<K, V> removeAtImpl(int index) {
            int index0 = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (index0 == index) {
                    AbstractWritableMapImpl.this.remove(e.getKey());
                    return e;
                }
                index0++;
            }
            return null;
        }

        @Override
        protected MapEntry<K, V> getImpl(int index) {
            int index0 = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (index0 == index) {
                    AbstractWritableMapImpl.this.remove(e.getKey());
                    return e;
                }
                index0++;
            }
            return null;
        }

        @Override
        protected int sizeImpl() {
            return AbstractWritableMapImpl.this.size();
        }

        @Override
        public Iterator<MapEntry<K, V>> iterator() {
            Iterator<Map.Entry<K, V>> it = entrySetImpl().iterator();
            return new Iterator<MapEntry<K, V>>() {
                Map.Entry<K, V> curr = null;

                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public MapEntry<K, V> next() {
                    curr = it.next();
                    return new MapEntryImpl<>(curr.getKey(), curr.getValue());
                }

                @Override
                public void remove() {
                    AbstractWritableMapImpl.this.remove(curr.getKey());
                }
            };
        }
    }

    private class ValueListListImpl extends WritableListBase<V> {

        public ValueListListImpl(String name, PropertyType elementType) {
            super(name, elementType);
        }

        @Override
        protected boolean addImpl(int index, V e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected V setImpl(int index, V e) {
            throw new IllegalArgumentException("Unsupported");
        }

        @Override
        protected int indexOfImpl(V item) {
            int index = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (Objects.equals(e.getValue(), item)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @Override
        protected V removeAtImpl(int index) {
            int index0 = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (index0 == index) {
                    V v = AbstractWritableMapImpl.this.remove(e.getKey());
                    return v;
                }
                index0++;
            }
            return null;
        }

        @Override
        protected V getImpl(int index) {
            int index0 = 0;
            for (MapEntry<K, V> e : AbstractWritableMapImpl.this) {
                if (index0 == index) {
                    return AbstractWritableMapImpl.this.get(e.getKey());
                }
                index0++;
            }
            return null;
        }

        @Override
        protected int sizeImpl() {
            return AbstractWritableMapImpl.this.size();
        }
    }

    @Override
    public String toString() {
        String p=isWritable()?"Writable":"ReadOnly";
        return p+ "Map("+fullPropertyName()+"){"
                + " values=[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + '}';
    }

}
