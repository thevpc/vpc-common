package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.PropertyType;
import net.vpc.common.prpbind.WritablePMap;

import java.util.Map;
import java.util.Set;

public class WritablePMapImpl<K, V> extends AbstractWritablePMapImpl<K, V>{
    private Map<K, V> value;

    public WritablePMapImpl(String name, PropertyType keyType, PropertyType valueType, Map<K, V> value) {
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
        return "WritablePMap{" +
                "name='" + getPropertyName() + '\'' +
                ", type=" + getType() +
                " value='" + value + '\''+
                '}';
    }


}
