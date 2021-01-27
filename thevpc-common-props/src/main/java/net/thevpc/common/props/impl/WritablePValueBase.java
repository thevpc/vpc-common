package net.thevpc.common.props.impl;


import java.util.Objects;
import java.util.function.Function;

import net.thevpc.common.props.*;
import net.thevpc.common.props.*;

public abstract class WritablePValueBase<T> extends AbstractProperty implements WritablePValue<T> {

    private T value;
    private PValue<T> ro;

    public WritablePValueBase(String name, PropertyType type, T value) {
        super(name, type);
        this.value = value;
    }

    @Override
    public <T2> void setAndBindConvert(WritablePValue<T2> other, Function<T, T2> map, Function<T2, T> mapBack) {
        T2 t2 = other.get();
        this.set(mapBack.apply(t2));
        bindConvert(other, map);
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
    public boolean isWritable() {
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
            if (old instanceof WithListeners) {
                listeners.removeDelegate((WithListeners) old);
            }
            if (v instanceof WithListeners) {
                listeners.addDelegate((WithListeners) v, () -> "/");
            }
            PropertyEvent event = new PropertyEvent(this, null, old, v, "/", PropertyUpdate.UPDATE);
            event = adjusters.firePropertyUpdated(event);
            if (event != null) {
                vetos.firePropertyUpdated(event);
                this.value = v;
                listeners.firePropertyUpdated(event);
            }
        }
    }

    @Override
    public PValue<T> readOnly() {
        if (ro == null) {
            ro = createReadOnly();
        }
        return ro;
    }

    protected abstract PValue<T> createReadOnly();

    @Override
    public String toString() {
        return "WritablePValue{"
                + "name='" + name() + '\''
                + ", type=" + type()
                + " value='" + value + '\''
                + '}';
    }

    static class BindPropertyListener<T> implements PropertyListener {

        private WritablePValue<T> target;

        public BindPropertyListener(WritablePValue<T> source) {
            this.target = source;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            target.set(event.getNewValue());
        }

        public boolean isTarget(PValue<T> source) {
            return source == this.target;
        }
    }

    static class BindPropertyConvertListener<T, T2> implements PropertyListener {

        private WritablePValue<T2> target;
        private Function<T, T2> map;
        private Function<T2, T> mapBack;

        public BindPropertyConvertListener(WritablePValue<T2> target, Function<T, T2> map, Function<T2, T> mapBack) {
            this.target = target;
            this.map = map;
            this.mapBack = mapBack;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            T t = event.getNewValue();
            target.set(map.apply(t));
        }

        public boolean isTarget(PValue<T> source) {
            return source == this.target;
        }
    }

    static <T> void helperBind(PValue<T> me, WritablePValue<T> other) {
        me.unbind(other);
        me.listeners().add(new WritablePValueBase.BindPropertyListener(other));
        other.set(me.get());
    }

    static <T, T2> void helperBindConvert(PValue<T> me, WritablePValue<T2> other, Function<T, T2> map) {
        me.unbind(other);
        me.listeners().add(new WritablePValueBase.BindPropertyConvertListener<>(other, map, null));
        T t = me.get();
        other.set(map.apply(t));
    }

    static <T2> void helperRemoveBindListeners(PropertyListeners listeners, WritablePValue<T2> other) {
        listeners.removeIf(x -> {
            if (x instanceof BindPropertyListener && ((BindPropertyListener) x).isTarget(other)) {
                return true;
            }
            if (x instanceof BindPropertyConvertListener && ((BindPropertyConvertListener) x).isTarget(other)) {
                return true;
            }
            return false;
        });
    }
}
