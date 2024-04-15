package net.thevpc.common.props;


import java.util.function.Function;
import java.util.function.Predicate;

public interface WritableMap<K, V> extends ObservableMap<K, V>,WritableProperty {

    void removeAll();

    void removeAll(Predicate<MapEntry<K, V>> a);

    V put(K index, V v);

    V computeIfAbsent(K k, Function<? super K, ? extends V> vf);

    V remove(K index);

    WritableList<V> values();


    PropertyVetos vetos();

}
