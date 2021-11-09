/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import java.util.function.Predicate;

/**
 *
 * @author thevpc
 */
public abstract class WritableLiMapAdapter<K, V> extends ObservableMapAdapter<K, V> implements WritableLiMap<K, V> {

    protected WritableLiMap<K, V> getWritableLiMapBase() {
        return (WritableLiMap<K, V>) getObservableMapBase();
    }

    @Override
    public void removeAll() {
        getWritableLiMapBase().removeAll();
    }

    @Override
    public void removeAll(Predicate<MapEntry<K, V>> a) {
        getWritableLiMapBase().removeAll(a);
    }

    @Override
    public V add(V v) {
        return getWritableLiMapBase().add(v);
    }

    @Override
    public V remove(K index) {
        return getWritableLiMapBase().remove(index);
    }

    @Override
    public WritableList<V> values() {
        return getWritableLiMapBase().values();
    }

    @Override
    public PropertyVetos vetos() {
        return getWritableLiMapBase().vetos();
    }

    @Override
    public PropertyAdjusters adjusters() {
        return getWritableLiMapBase().adjusters();
    }
}
