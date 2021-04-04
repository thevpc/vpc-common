package net.thevpc.common.props.impl;


import java.util.Objects;
import net.thevpc.common.props.*;
import net.thevpc.common.props.MapEntry;

public class PMapEntryImpl<K,V> implements MapEntry<K,V> {
    private K k;
    private V v;

    public PMapEntryImpl(K k, V v) {
        this.k = k;
        this.v = v;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PMapEntryImpl<?, ?> pMapEntry = (PMapEntryImpl<?, ?>) o;
        return Objects.equals(k, pMapEntry.k) &&
                Objects.equals(v, pMapEntry.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, v);
    }

    @Override
    public String toString() {
        return "(" +
                "k=" + k +
                ", v=" + v +
                ')';
    }
}
