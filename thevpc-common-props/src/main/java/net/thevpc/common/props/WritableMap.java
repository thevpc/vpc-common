package net.thevpc.common.props;


import java.util.function.Predicate;

public interface WritableMap<K, V> extends ObservableMap<K, V> {

    void removeAll();

    void removeAll(Predicate<MapEntry<K, V>> a);

    V put(K index, V v);

    V remove(K index);

    WritableList<V> values();


    PropertyVetos vetos();

}
