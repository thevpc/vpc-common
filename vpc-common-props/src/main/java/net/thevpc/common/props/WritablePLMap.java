package net.thevpc.common.props;

import java.util.function.Predicate;

public interface WritablePLMap<K, V> extends PMap<K, V> {

    void removeAll();

    void removeAll(Predicate<PMapEntry<K, V>> a);

    V add(V v);

    V remove(K index);

    WritablePList<V> values();


    PropertyVetos vetos();

}
