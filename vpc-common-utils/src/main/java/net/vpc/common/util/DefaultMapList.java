package net.vpc.common.util;

import java.util.*;

/**
 * Created by vpc on 6/1/16.
 */
public class DefaultMapList<K,V> implements MapList<K,V>{
    private Map<K,Integer> map=new LinkedHashMap<K, Integer>();
    private List<V> list=new LinkedList<V>();
    private Converter<V,K> converter;

//    public DefaultMapList(Function<V, K> converter) {
//        this.converter = new Converter<K, V>() {
//            @Override
//            public K convert(V value) {
//                return converter.apply(value);
//            }
//        };
//    }

    public DefaultMapList(Converter<V,K> converter) {
        this.converter = converter;
    }

    public DefaultMapList(List<V> list, Converter<V,K> converter) {
        this.converter = converter;
        addAll(list);
    }

    public boolean add(V v){
        K k = converter.convert(v);
        if(map.containsKey(k)) {
            int pos=map.get(k);
            list.set(pos,v);
        }else{
            map.put(k, list.size());
            list.add(v);
        }
        return true;
    }

//    @Override
    public int size() {
        return list.size();
    }

//    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

//    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

//    @Override
    public Iterator<V> iterator() {
        return list.iterator();
    }

//    @Override
    public Object[] toArray() {
        return list.toArray();
    }

//    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

//    @Override
    public boolean remove(Object o) {
        K k = converter.convert((V) o);
        Integer pos = map.get(k);
        if(pos!=null){
            list.remove(pos.intValue());
            map.remove(k);
            return true;
        }
        return false;
    }

//    @Override
    public V removeByKey(K k) {
        Integer pos = map.get(k);
        if(pos!=null){
            V v=list.remove(pos.intValue());
            map.remove(k);
            return v;
        }
        return null;
    }

//    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

//    @Override
    public boolean addAll(Collection<? extends V> c) {
        boolean a=false;
        for (V v : c) {
            if(add(v)){
                a=true;
            }
        }
        return a;
    }

//    @Override
    public boolean addAll(int index, Collection<? extends V> c) {
        if(index==size()){
            return addAll(c);
        }else{
            throw new IllegalArgumentException("Not supported");
        }
    }

//    @Override
    public boolean removeAll(Collection<?> c) {
        throw new IllegalArgumentException("Not supported");
    }

//    @Override
    public boolean retainAll(Collection<?> c) {
        throw new IllegalArgumentException("Not supported");
    }

//    @Override
    public void clear() {
        map.clear();
        list.clear();
    }

//    @Override
    public V get(int index) {
        return list.get(index);
    }

    public V getByKey(K k) {
        Integer b = map.get(k);
        return b==null?null:list.get(b.intValue());
    }

    public V get(K k) {
        Integer b = map.get(k);
        return b==null?null:list.get(b.intValue());
    }

//    @Override
    public V set(int index, V element) {
        V v = list.get(index);
        K k= converter.convert(v);
        map.remove(k);

        k= converter.convert(element);
        map.put(k, index);
        list.set(index,element);
        return v;
    }

//    @Override
    public void add(int index, V element) {
        if(index==size()){
            add(element);
        }else{
            throw new IllegalArgumentException("Not supported");
        }
    }

//    @Override
    public V remove(int index) {
        V v = list.get(index);
        K k= converter.convert(v);
        map.remove(k);
        return v;
    }

//    @Override
    public int indexOf(Object o) {
        K k = converter.convert((V) o);
        return map.get(k);
    }

//    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

//    @Override
    public ListIterator<V> listIterator() {
        return list.listIterator();
    }

//    @Override
    public ListIterator<V> listIterator(int index) {
        return null;
    }

//    @Override
    public List<V> subList(int fromIndex, int toIndex) {
        return new DefaultMapList<K,V>(list.subList(fromIndex,toIndex), converter);
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsMappedValue(V object) {
        return map.containsKey(converter.convert(object));
    }

    public V getByValue(V object) {
        return getByKey(converter.convert(object));
    }

    public boolean containsValue(Object value) {
        return contains(value);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    private class ReadOnlyIterator<X> implements Iterator<X>{
        Iterator<X> x;

        public ReadOnlyIterator(Iterator<X> x) {
            this.x = x;
        }

//        @Override
        public boolean hasNext() {
            return x.hasNext();
        }

//        @Override
        public X next() {
            return x.next();
        }

//        @Override
        public void remove() {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    private class ReadOnlyListIterator<X> extends ReadOnlyIterator<X> implements ListIterator<X>{
        public ReadOnlyListIterator(ListIterator<X> x) {
            super(x);
        }
        private ListIterator<X> li(){
            return (ListIterator<X>)x;
        }

//        @Override
        public boolean hasPrevious() {
            return li().hasPrevious();
        }

//        @Override
        public X previous() {
            return li().previous();
        }

//        @Override
        public int nextIndex() {
            return li().nextIndex();
        }

//        @Override
        public int previousIndex() {
            return li().previousIndex();
        }

//        @Override
        public void set(X x) {
            throw new IllegalArgumentException("Unsupported");
        }

//        @Override
        public void add(X x) {
            throw new IllegalArgumentException("Unsupported");
        }
    }
}
