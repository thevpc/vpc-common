package net.thevpc.common.props.impl;


import java.util.List;

import net.thevpc.common.props.*;

public class PDispatcherImpl<T> extends WritablePropertyBase implements WritableDispatcher<T> {

    private long index;
    private ObservableDispatcher<T> ro;

    public PDispatcherImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType));
    }

    @Override
    public ObservableDispatcher<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyDispatcher<>(this);
        }
        return ro;
    }

    @Override
    public void add(T v) {
        if (v instanceof Property) {
            listeners.addDelegate((Property) v, () -> Path.of(String.valueOf(index)));
        }
        ((DefaultPropertyListeners)listeners).firePropertyUpdated(new PropertyEvent(
                this,
                index,
                null,
                v,
                Path.root().append(String.valueOf(index)),
                PropertyUpdate.ADD,true
        ));
        index++;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public String toString() {
        return "PDispatcher{"
                + "name='" + fullPropertyName() + '\''
                + ", type=" + propertyType()
                + '}';
    }

}
