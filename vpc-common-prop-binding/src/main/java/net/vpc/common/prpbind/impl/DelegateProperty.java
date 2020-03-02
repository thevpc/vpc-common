package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

public abstract class DelegateProperty<T> implements Property {
    protected PropertyListenersImpl listeners;
    protected Property base;

    public DelegateProperty(Property base) {
        this.base = base;
        listeners=new PropertyListenersImpl(this);
        base.listeners().add(new DelegatePropertyListener());
    }

    public Property getBase() {
        return base;
    }

    @Override
    public PropertyType getType() {
        return getBase().getType();
    }

    @Override
    public boolean isRefWritable() {
        return getBase().isRefWritable();
    }

    @Override
    public boolean isValueWritable() {
        return getBase().isValueWritable();
    }

    public String getPropertyName() {
        return getBase().getPropertyName();
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    private class DelegatePropertyListener implements PropertyListener {
        @Override
        public void propertyUpdated(PropertyEvent event) {
            listeners.firePropertyUpdated(event);
        }
    }
}
