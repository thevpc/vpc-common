package net.thevpc.common.props.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

import net.thevpc.common.props.*;
import net.thevpc.common.props.*;

public class WritablePIndexedNodeImpl<T> extends WritablePValueBase<T> implements WritablePIndexedNode<T> {

    private WritablePList<WritablePIndexedNode<T>> children;

    public WritablePIndexedNodeImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType), null);
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
    public boolean isWritable() {
        return true;
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

    @Override
    public PIndexedNode<T> readOnly() {
        return (PIndexedNode<T>) super.readOnly();
    }

    @Override
    protected PValue<T> createReadOnly() {
        return new ReadOnlyPIndexedNode<T>(this);
    }

    //    @Override
    public WritablePList<WritablePIndexedNode<T>> children() {
        return children;
    }

    @Override
    public String toString() {
        return "WritableNode{"
                + "name='" + name() + '\''
                + ", type=" + type()
                + " value='" + get() + '\''
                + '}';
    }

}
