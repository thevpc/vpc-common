package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

import java.util.*;
import java.util.function.Predicate;

public class WritablePNamedNodeImpl<T> extends AbstractProperty implements WritablePNamedNode<T> {
    private T value;
    private WritablePMap<String, WritablePNamedNode<T>> children;
    private PNamedNode<T> ro;

    public WritablePNamedNodeImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType));
        PropertyType type2 = PropertyType.of(WritablePList.class,
                PropertyType.of(WritablePNamedNode.class, elementType)
        );
        this.children = Props.of(name).mapOf(PropertyType.of(String.class), type2);
        this.children.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                WritablePNamedNodeImpl.this.listeners.firePropertyUpdated(
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
    public Map<String, WritablePNamedNode<T>> findChildren(Predicate<WritablePNamedNode<T>> filter, boolean deep) {
        Map<String, WritablePNamedNode<T>> result = new HashMap<>();
        if (deep) {
            Stack<PMapEntry<String, WritablePNamedNode<T>>> stack = new Stack<>();
            for (PMapEntry<String, WritablePNamedNode<T>> child : children) {
                stack.add(child);
            }
            while (!stack.isEmpty()) {
                PMapEntry<String, WritablePNamedNode<T>> p = stack.pop();
                if (filter.test(p.getValue())) {
                    result.put(p.getKey(), p.getValue());
                }
                for (PMapEntry<String, WritablePNamedNode<T>> child : (WritablePMap<String, WritablePNamedNode<T>>) p.getValue().children()) {
                    stack.push(child);
                }
            }
        } else {
            for (PMapEntry<String, WritablePNamedNode<T>> p : children) {
                if (filter.test(p.getValue())) {
                    result.put(p.getKey(), p.getValue());
                }
            }
        }
        return result;
    }

    //    @Override
    public void set(T v) {
        T old = this.value;
        if (!Objects.equals(old, v)) {
            if (old instanceof WithListeners) {
                listeners.removeDelegate((WithListeners) old);
            }
            if (v instanceof WithListeners) {
                listeners.addDelegate((WithListeners) v, () -> "/");
            }

            PropertyEvent event = new PropertyEvent(this, null, old, v, "/", PropertyUpdate.UPDATE);
            vetos.firePropertyUpdated(event);
            this.value = v;
            listeners.firePropertyUpdated(event);
        }
    }

    @Override
    public PNamedNode<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyPNamedNode<T>(this);
        }
        return ro;
    }


    //    @Override
    public WritablePMap<String, WritablePNamedNode<T>> children() {
        return children;
    }

    @Override
    public String toString() {
        return "WritableNamedNode{" +
                "name='" + getPropertyName() + '\'' +
                ", type=" + getType() +
                " value='" + value + '\'' +
                '}';
    }

}
