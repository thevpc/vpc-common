package net.thevpc.common.props.impl;


import java.util.List;

import net.thevpc.common.props.*;
import net.thevpc.common.props.*;

public class PDispatcherImpl<T> extends AbstractProperty implements WritablePDispatcher<T> {

    private long index;
    private PDispatcher<T> ro;

    public PDispatcherImpl(String name, PropertyType elementType) {
        super(name, PropertyType.of(List.class, elementType));
    }

    @Override
    public PDispatcher<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyPDispatcher<>(this);
        }
        return ro;
    }

    @Override
    public void add(T v) {
        if (v instanceof WithListeners) {
            listeners.addDelegate((WithListeners) v, () -> "/" + index);
        }
        listeners.firePropertyUpdated(new PropertyEvent(
                this,
                index,
                null,
                v,
                "/" + index,
                PropertyUpdate.ADD
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
                + "name='" + name() + '\''
                + ", type=" + type()
                + '}';
    }

}
