package net.thevpc.common.props.impl;

import net.thevpc.common.props.PropertyType;

import java.util.Map;
import java.util.Set;

public class WritableMapImpl<K, V> extends AbstractWritableMapImpl<K, V>{
    private Map<K, V> value;

    public WritableMapImpl(String name, PropertyType keyType, PropertyType valueType, Map<K, V> value) {
        super(name, keyType, valueType);
        this.value = value;
    }

    @Override
    protected Set<Map.Entry<K,V>> entrySetImpl() {
        return value.entrySet();
    }

    @Override
    protected boolean containsKeyImpl(K k) {
        return value.containsKey(k);
    }

    @Override
    protected int sizeImpl() {
        return value.size();
    }

    @Override
    protected V getImpl(K k) {
        return value.get(k);
    }

    @Override
    protected V putImpl(K k, V v) {
        return value.put(k, v);
    }

    @Override
    protected V removeImpl(K k) {
        return value.remove(k);
    }

    @Override
    public String toString() {
        String p=isWritable()?"Writable":"ReadOnly";
        return p+ "Map("+fullPropertyName()+"){" +
                " values='" + value + '\''+
                '}';
    }


}
