package net.vpc.common.props.impl;

import net.vpc.common.props.PList;
import net.vpc.common.props.PIndexedNode;
import net.vpc.common.props.WritablePIndexedNode;
import net.vpc.common.props.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.vpc.common.props.WritablePValue;
import static net.vpc.common.props.impl.WritablePValueBase.helperBind;
import static net.vpc.common.props.impl.WritablePValueBase.helperBindConvert;
import static net.vpc.common.props.impl.WritablePValueBase.helperRemoveBindListeners;

public class ReadOnlyPIndexedNode<T> extends DelegateProperty implements PIndexedNode<T> {

    public ReadOnlyPIndexedNode(PIndexedNode<T> base) {
        super(base);
    }

    @Override
    public void bind(WritablePValue<T> other) {
        helperBind(this, other);
    }

    @Override
    public <T2> void bindConvert(WritablePValue<T2> other, Function<T, T2> map) {
        helperBindConvert(this, other, map);
    }

    @Override
    public <T2> void unbind(WritablePValue<T2> other) {
        helperRemoveBindListeners(listeners(), other);
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
