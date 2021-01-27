package net.thevpc.common.props.impl;


import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import net.thevpc.common.props.PMap;
import net.thevpc.common.props.PNamedNode;
import net.thevpc.common.props.WritablePNamedNode;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.common.props.*;

public class ReadOnlyPNamedNode<T> extends DelegateProperty implements PNamedNode<T> {

    public ReadOnlyPNamedNode(PNamedNode<T> base) {
        super(base);
    }

    @Override
    public void bind(WritablePValue<T> other) {
        WritablePValueBase.helperBind(this, other);
    }

    @Override
    public <T2> void bindConvert(WritablePValue<T2> other, Function<T, T2> map) {
        WritablePValueBase.helperBindConvert(this, other, map);
    }

    @Override
    public <T2> void unbind(WritablePValue<T2> other) {
        WritablePValueBase.helperRemoveBindListeners(listeners(), other);
    }

    @Override
    public PNamedNode<T> readOnly() {
        return this;
    }

    @Override
    public PNamedNode<T> getBase() {
        return (PNamedNode<T>) super.getBase();
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
    public Map<String, WritablePNamedNode<T>> findChildren(Predicate<WritablePNamedNode<T>> filter, boolean deep) {
        return getBase().findChildren(filter, deep);
    }

    @Override
    public PMap<String, PNamedNode<T>> children() {
        return new ReadOnlyPMap<String, PNamedNode<T>>((PMap) getBase().children()) {
            @Override
            public PNamedNode<T> get(String index) {
                PNamedNode<T> n = super.get(index);
                if (n.isWritable()) {
                    return ((WritablePNamedNode) n).readOnly();
                }
                return n;
            }
        };
    }
}
