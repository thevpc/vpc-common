package net.thevpc.common.collections;

import java.util.*;

/**
 * a Map that implements List interface
 * was named MapList
 */
public interface KeyValueList<K,V> extends List<V>, Collection2{

    public V getByKey(K k) ;

    public V removeByKey(K k) ;

    public boolean containsKey(Object key);

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     *         specified value
     * @throws ClassCastException if the value is of an inappropriate type for
     *         this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *         map does not permit null values
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public boolean containsMappedValue(V value);

    /**
     * returns, it it exists, the value in this list with the same key for value param
     * @param value value whose key presence in this map is to be tested
     * @return value of the list equivalent to the given value
     */
    public V getByValue(V value);

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param value element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *         list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    public boolean containsValue(Object value);

    public Set<K> keySet() ;

    public default KeyValueList<K,V> append(V v) {
        add(v);
        return this;
    }

    public default KeyValueList<K,V> appendAll(Collection<V> v) {
        addAll(v);
        return this;
    }

}
