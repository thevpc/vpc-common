package net.vpc.common.props.impl;


import java.util.*;
import java.util.function.Predicate;
import net.vpc.common.props.*;

public class WritablePNamedNodeImpl<T> extends WritablePValueBase<T> implements WritablePNamedNode<T> {

    private WritablePMap<String, WritablePNamedNode<T>> children;

    public WritablePNamedNodeImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType), null);
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
    public boolean isWritable() {
        return true;
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

    @Override
    public PNamedNode<T> readOnly() {
        return (PNamedNode<T>) super.readOnly();
    }

    @Override
    protected PValue<T> createReadOnly() {
        return new ReadOnlyPNamedNode<T>(this);
    }

    //    @Override
    public WritablePMap<String, WritablePNamedNode<T>> children() {
        return children;
    }

    @Override
    public String toString() {
        return "WritableNamedNode{"
                + "name='" + name() + '\''
                + ", type=" + type()
                + " value='" + get() + '\''
                + '}';
    }

}
