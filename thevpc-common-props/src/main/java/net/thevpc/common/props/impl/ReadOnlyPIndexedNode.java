package net.thevpc.common.props.impl;

import net.thevpc.common.props.PList;
import net.thevpc.common.props.PIndexedNode;
import net.thevpc.common.props.WritablePIndexedNode;
import net.thevpc.common.props.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.thevpc.common.props.WritablePValue;

public class ReadOnlyPIndexedNode<T> extends DelegateProperty implements PIndexedNode<T> {

    public ReadOnlyPIndexedNode(PIndexedNode<T> base) {
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
    public PIndexedNode<T> getBase() {
        return (PIndexedNode<T>) super.getBase();
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
    public List<WritablePIndexedNode<T>> findChildren(Predicate<WritablePIndexedNode<T>> filter, boolean deep) {
        return getBase().findChildren(filter, deep);
    }

    @Override
    public PList<PIndexedNode<T>> children() {
        return new ReadOnlyPList<PIndexedNode<T>>((PList) getBase().children()) {
            @Override
            public PIndexedNode<T> get(int index) {
                PIndexedNode<T> n = super.get(index);
                if (n.isWritable()) {
                    return ((WritablePIndexedNode) n).readOnly();
                }
                return n;
            }
        };
    }

    @Override
    public PIndexedNode<T> readOnly() {
        return this;
    }
    
}
