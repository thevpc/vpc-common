package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ReadOnlyPNamedNode<T> extends DelegateProperty implements PNamedNode<T> {
    public ReadOnlyPNamedNode(PNamedNode<T> base) {
        super(base);
    }

    @Override
    public PNamedNode<T> getBase() {
        return (PNamedNode<T>) super.getBase();
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
    public Map<String,WritablePNamedNode<T>> findChildren(Predicate<WritablePNamedNode<T>> filter, boolean deep) {
        return getBase().findChildren(filter, deep);
    }

    @Override
    public PMap<String,PNamedNode<T>> children() {
        return new ReadOnlyPMap<String,PNamedNode<T>>((PMap) getBase().children()) {
            @Override
            public PNamedNode<T> get(String index) {
                PNamedNode<T> n = super.get(index);
                if (n.isValueWritable()) {
                    return ((WritablePNamedNode) n).readOnly();
                }
                return n;
            }
        };
    }
}
