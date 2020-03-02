package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.PList;
import net.vpc.common.prpbind.PIndexedNode;
import net.vpc.common.prpbind.WritablePIndexedNode;

import java.util.List;
import java.util.function.Predicate;

public class ReadOnlyPIndexedNode<T> extends DelegateProperty implements PIndexedNode<T> {
    public ReadOnlyPIndexedNode(PIndexedNode<T> base) {
        super(base);
    }

    @Override
    public PIndexedNode<T> getBase() {
        return (PIndexedNode<T>) super.getBase();
    }

    @Override
    public boolean isRefWritable() {
        return false;
    }

    @Override
    public boolean isValueWritable() {
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
                if (n.isValueWritable()) {
                    return ((WritablePIndexedNode) n).readOnly();
                }
                return n;
            }
        };
    }
}
