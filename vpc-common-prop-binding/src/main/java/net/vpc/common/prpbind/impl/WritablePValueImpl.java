package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

import java.util.Objects;

public class WritablePValueImpl<T> extends AbstractProperty implements WritablePValue<T> {
    private T value;
    private PValue<T> ro;

    public WritablePValueImpl(String name, PropertyType type, T value) {
        super(name, type);
        this.value = value;
        if (type.getArgs().length > 0) {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    @Override
    public boolean isRefWritable() {
        return true;
    }

    @Override
    public boolean isValueWritable() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
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
            event = adjusters.firePropertyUpdated(event);
            if(event!=null) {
                vetos.firePropertyUpdated(event);
                this.value = v;
                listeners.firePropertyUpdated(event);
            }
        }
    }

    @Override
    public PValue<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyPValue<>(this);
        }
        return ro;
    }

    @Override
    public String toString() {
        return "WritablePValue{" +
                "name='" + getPropertyName() + '\'' +
                ", type=" + getType() +
                " value='" + value + '\''+
                '}';
    }
}
