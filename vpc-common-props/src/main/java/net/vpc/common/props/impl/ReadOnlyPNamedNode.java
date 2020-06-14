package net.vpc.common.props.impl;


import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import static net.vpc.common.props.impl.WritablePValueBase.helperBind;
import static net.vpc.common.props.impl.WritablePValueBase.helperBindConvert;
import static net.vpc.common.props.impl.WritablePValueBase.helperRemoveBindListeners;
import net.vpc.common.props.*;

public class ReadOnlyPNamedNode<T> extends DelegateProperty implements PNamedNode<T> {

    public ReadOnlyPNamedNode(PNamedNode<T> base) {
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
