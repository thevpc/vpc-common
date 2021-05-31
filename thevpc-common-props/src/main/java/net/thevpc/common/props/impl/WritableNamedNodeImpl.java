package net.thevpc.common.props.impl;


import java.util.*;
import java.util.function.Predicate;

import net.thevpc.common.props.*;

public class WritableNamedNodeImpl<T> extends WritableValueBase<T> implements WritableNamedNode<T> {

    private WritableMap<String, WritableNamedNode<T>> children;

    public WritableNamedNodeImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType), (T) null);
        PropertyType type2 = PropertyType.of(WritableList.class,
                PropertyType.of(WritableNamedNode.class, elementType)
        );
        this.children = Props.of(name).mapOf(PropertyType.of(String.class), type2);
        this.children.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                ((DefaultPropertyListeners)WritableNamedNodeImpl.this.listeners).firePropertyUpdated(
                        PropsHelper.prefixPath(event, Path.root())
                );
            }
        });
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public Map<String, WritableNamedNode<T>> findChildren(Predicate<WritableNamedNode<T>> filter, boolean deep) {
        Map<String, WritableNamedNode<T>> result = new HashMap<>();
        if (deep) {
            Stack<MapEntry<String, WritableNamedNode<T>>> stack = new Stack<>();
            for (MapEntry<String, WritableNamedNode<T>> child : children) {
                stack.add(child);
            }
            while (!stack.isEmpty()) {
                MapEntry<String, WritableNamedNode<T>> p = stack.pop();
                if (filter.test(p.getValue())) {
                    result.put(p.getKey(), p.getValue());
                }
                for (MapEntry<String, WritableNamedNode<T>> child : (WritableMap<String, WritableNamedNode<T>>) p.getValue().children()) {
                    stack.push(child);
                }
            }
        } else {
            for (MapEntry<String, WritableNamedNode<T>> p : children) {
                if (filter.test(p.getValue())) {
                    result.put(p.getKey(), p.getValue());
                }
            }
        }
        return result;
    }

    @Override
    public NamedNode<T> readOnly() {
        return (NamedNode<T>) super.readOnly();
    }

    @Override
    protected ObservableValue<T> createReadOnly() {
        return new ReadOnlyPNamedNode<T>(this);
    }

    //    @Override
    public WritableMap<String, WritableNamedNode<T>> children() {
        return children;
    }

    @Override
    public String toString() {
        return "WritableNamedNode{"
                + "name='" + fullPropertyName() + '\''
                + ", type=" + propertyType()
                + " value='" + get() + '\''
                + '}';
    }

}
