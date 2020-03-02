package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;

public class WritablePIndexedNodeImpl<T> extends AbstractProperty implements WritablePIndexedNode<T> {
    private T value;
    private WritablePList<WritablePIndexedNode<T>> children;
    private PIndexedNode<T> ro;

    public WritablePIndexedNodeImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType));
        PropertyType type2 = PropertyType.of(WritablePList.class,
                PropertyType.of(WritablePIndexedNode.class, elementType)
        );
        this.children = Props.of(name).listOf(type2);
        this.children.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                WritablePIndexedNodeImpl.this.listeners.firePropertyUpdated(
                        PropsHelper.prefixPath(event, "/")
                );
            }
        });
    }

    @Override
    public boolean isRefWritable() {
        return false;
    }

    @Override
    public boolean isValueWritable() {
        return true;
    }

    //    @Override
    public T get() {
        return value;
    }

    @Override
    public List<WritablePIndexedNode<T>> findChildren(Predicate<WritablePIndexedNode<T>> filter, boolean deep) {
        List<WritablePIndexedNode<T>> result = new ArrayList<>();
        if (deep) {
            Stack<WritablePIndexedNode<T>> stack = new Stack<>();
            for (WritablePIndexedNode<T> child : children) {
                stack.add(child);
            }
            while (!stack.isEmpty()) {
                WritablePIndexedNode<T> p = stack.pop();
                if (filter.test(p)) {
                    result.add(p);
                }
                for (WritablePIndexedNode<T> child : p.children()) {
                    stack.push(child);
                }
            }
        } else {
            for (WritablePIndexedNode<T> child : children) {
                if (filter.test(child)) {
                    result.add(child);
                }
            }
        }
        return result;
    }

    //    @Override
    public void set(T v) {
        T old = this.value;
        if (!Objects.equals(old, v)) {
            if(old instanceof WithListeners){
                listeners.removeDelegate((WithListeners) old);
            }
            if(v instanceof WithListeners){
                listeners.addDelegate((WithListeners) v, () -> "/");
            }

            PropertyEvent event = new PropertyEvent(this, null, old, v, "/", PropertyUpdate.UPDATE);
            vetos.firePropertyUpdated(event);
            this.value = v;
            listeners.firePropertyUpdated(event);
        }
    }

    @Override
    public PIndexedNode<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyPIndexedNode<T>(this);
        }
        return ro;
    }


    //    @Override
    public WritablePList<WritablePIndexedNode<T>> children() {
        return children;
    }

    @Override
    public String toString() {
        return "WritableNode{" +
                "name='" + getPropertyName() + '\'' +
                ", type=" + getType() +
                " value='" + value + '\'' +
                '}';
    }

}
