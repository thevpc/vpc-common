package net.vpc.common.prpbind;


import java.util.Set;
import java.util.function.Predicate;

public interface WritablePMap<K, V> extends PMap<K, V> {

    void removeAll();

    void removeAll(Predicate<PMapEntry<K, V>> a);

    V put(K index, V v);

    V remove(K index);

    WritablePList<V> values();

    PMap<K, V> readOnly();

    PropertyVetos vetos();

}
