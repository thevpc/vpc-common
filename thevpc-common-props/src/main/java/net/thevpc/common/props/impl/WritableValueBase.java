package net.thevpc.common.props.impl;


import java.util.Objects;

import net.thevpc.common.props.*;

public class WritableValueBase<T> extends ObservableValueBase<T> implements WritableValue<T>{

    private ObservableValue<T> ro;
    protected PropertyVetosImpl vetos = new PropertyVetosImpl(this);
    protected PropertyAdjustersImpl adjusters = new PropertyAdjustersImpl(this);


    public WritableValueBase(String name, PropertyType type, T value) {
        super(name, type,new DefaultWritableValueModel<>(value));
    }
    public WritableValueBase(String name, PropertyType type, WritableValueModel<T> value) {
        super(name, type,value);
    }

    @Override
    public boolean isWritable() {
        return true;
    }

//    @Override
//    public String toString() {
//        return String.valueOf(get());
//    }

    protected WritableValueModel<T> model(){
        return (WritableValueModel<T>) super.model();
    }

    @Override
    public T get() {
        return model().get();
    }

    @Override
    public void set(T v) {
        T old = this.model().get();
        if (!Objects.equals(old, v)) {
            if (old instanceof Property) {
                listeners.removeDelegate((Property) old);
            }
            if (v instanceof Property) {
                listeners.addDelegate((Property) v, () -> Path.root());
            }
            PropertyEvent event = new PropertyEvent(this, null, old, v, currentPath(), PropertyUpdate.UPDATE,true);
            PropertyAdjusterContext q = adjusters.firePropertyUpdated(event);
            for (Runnable runnable : q.getBefore()) {
                runnable.run();
            }
            if(!q.isIgnore()){
                vetos.firePropertyUpdated(q.event());
                this.model().set(v);
                ((DefaultPropertyListeners)listeners).firePropertyUpdated(q.event());
            }
            for (Runnable runnable : q.getAfter()) {
                runnable.run();
            }
        }
    }
    protected Path currentPath(){
        return Path.of(propertyName());
    }

    @Override
    public ObservableValue<T> readOnly() {
        if (ro == null) {
            ro = createReadOnly();
        }
        return ro;
    }

    protected ObservableValue<T> createReadOnly() {
        if(isWritable()){
            return new ReadOnlyValue<>(this);
        }
        return this;
    }

    @Override
    public PropertyAdjusters adjusters() {
        return adjusters;
    }

    public PropertyVetos vetos() {
        return vetos;
    }
}
