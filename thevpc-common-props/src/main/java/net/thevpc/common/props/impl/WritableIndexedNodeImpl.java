package net.thevpc.common.props.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;

import net.thevpc.common.props.*;

public class WritableIndexedNodeImpl<T> extends WritableValueBase<T> implements WritableIndexedNode<T> {

    private WritableList<WritableIndexedNode<T>> children;

    public WritableIndexedNodeImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType), (T) null);
        PropertyType type2 = PropertyType.of(WritableList.class,
                PropertyType.of(WritableIndexedNode.class, elementType)
        );
        this.children = Props.of(name).listOf(type2);
        this.children.userObjects().put("owner",this);
        this.children.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                ((DefaultPropertyListeners)WritableIndexedNodeImpl.this.listeners).firePropertyUpdated(
                        PropsHelper.prefixPath(event, Path.root())
                );
            }
        });
        propagateEvents(children);
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public List<WritableIndexedNode<T>> findChildren(Predicate<WritableIndexedNode<T>> filter, boolean deep) {
        List<WritableIndexedNode<T>> result = new ArrayList<>();
        if (deep) {
            Stack<WritableIndexedNode<T>> stack = new Stack<>();
            for (WritableIndexedNode<T> child : children) {
                stack.add(child);
            }
            while (!stack.isEmpty()) {
                WritableIndexedNode<T> p = stack.pop();
                if (filter.test(p)) {
                    result.add(p);
                }
                for (WritableIndexedNode<T> child : p.children()) {
                    stack.push(child);
                }
            }
        } else {
            for (WritableIndexedNode<T> child : children) {
                if (filter.test(child)) {
                    result.add(child);
                }
            }
        }
        return result;
    }

    @Override
    public IndexedNode<T> readOnly() {
        return (IndexedNode<T>) super.readOnly();
    }

    @Override
    protected ObservableValue<T> createReadOnly() {
        return new ReadOnlyPIndexedNode<T>(this);
    }

    //    @Override
    public WritableList<WritableIndexedNode<T>> children() {
        return children;
    }

    @Override
    public String toString() {
        return "WritableNode{"
                + "name='" + fullPropertyName() + '\''
                + ", type=" + propertyType()
                + " value='" + get() + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WritableIndexedNodeImpl<?> that = (WritableIndexedNodeImpl<?>) o;
        return Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), children);
    }
}
