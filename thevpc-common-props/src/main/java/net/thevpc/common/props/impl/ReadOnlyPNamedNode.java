package net.thevpc.common.props.impl;


import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import net.thevpc.common.props.*;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.NamedNode;
import net.thevpc.common.props.WritableNamedNode;
import net.thevpc.common.props.ObservableMap;

public class ReadOnlyPNamedNode<T> extends DelegateProperty implements NamedNode<T> {

    public ReadOnlyPNamedNode(NamedNode<T> base) {
        super(base);
    }

    @Override
    public void bind(WritableValue<T> other) {
        WritableValueBase.helperBind(this, other);
    }

    @Override
    public <T2> void bindConvert(WritableValue<T2> other, Function<T, T2> map) {
        WritableValueBase.helperBindConvert(this, other, map);
    }

    @Override
    public <T2> void unbind(WritableValue<T2> other) {
        WritableValueBase.helperRemoveBindListeners(listeners(), other);
    }

    @Override
    public NamedNode<T> readOnly() {
        return this;
    }

    @Override
    public NamedNode<T> getBase() {
        return (NamedNode<T>) super.getBase();
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    //    @Override
    public T get() {
        return getBase().get();
    }

    @Override
    public Map<String, WritableNamedNode<T>> findChildren(Predicate<WritableNamedNode<T>> filter, boolean deep) {
        return getBase().findChildren(filter, deep);
    }

    @Override
    public ObservableMap<String, NamedNode<T>> children() {
        return new ReadOnlyMap<String, NamedNode<T>>((ObservableMap) getBase().children()) {
            @Override
            public NamedNode<T> get(String index) {
                NamedNode<T> n = super.get(index);
                if (n.isWritable()) {
                    return ((WritableNamedNode) n).readOnly();
                }
                return n;
            }
        };
    }
}
