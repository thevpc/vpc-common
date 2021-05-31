package net.thevpc.common.props;

import java.util.function.Predicate;

public interface WritableLiMap<K, V> extends ObservableMap<K, V>,WritableProperty {

    void removeAll();

    void removeAll(Predicate<MapEntry<K, V>> a);

    V add(V v);

    V remove(K index);

    WritableList<V> values();


    PropertyVetos vetos();

}
