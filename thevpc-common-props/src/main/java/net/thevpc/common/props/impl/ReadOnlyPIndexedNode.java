package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.thevpc.common.props.WritableIndexedNode;
import net.thevpc.common.props.IndexedNode;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.ObservableList;

public class ReadOnlyPIndexedNode<T> extends DelegateProperty implements IndexedNode<T> {

    public ReadOnlyPIndexedNode(IndexedNode<T> base) {
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
    public IndexedNode<T> getBase() {
        return (IndexedNode<T>) super.getBase();
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
    public List<WritableIndexedNode<T>> findChildren(Predicate<WritableIndexedNode<T>> filter, boolean deep) {
        return getBase().findChildren(filter, deep);
    }

    @Override
    public ObservableList<IndexedNode<T>> children() {
        return new ReadOnlyList<IndexedNode<T>>((ObservableList) getBase().children()) {
            @Override
            public IndexedNode<T> get(int index) {
                IndexedNode<T> n = super.get(index);
                if (n.isWritable()) {
                    return ((WritableIndexedNode) n).readOnly();
                }
                return n;
            }
        };
    }

    @Override
    public IndexedNode<T> readOnly() {
        return this;
    }
    
}
