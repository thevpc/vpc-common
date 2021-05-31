package net.thevpc.common.props.impl;
import net.thevpc.common.props.*;

public abstract class PropertyDelegate implements Property {

    protected DefaultPropertyListeners listeners;
    protected Property base;
    protected Property parentProperty;

    public PropertyDelegate(Property base) {
        this.base = base;
        listeners = new DefaultPropertyListeners(this);
        base.onChange(new DelegatePropertyListener());
    }

    protected Property getBase() {
        return base;
    }

    @Override
    public PropertyType propertyType() {
        return getBase().propertyType();
    }

    @Override
    public boolean isWritable() {
        return getBase().isWritable();
    }

    @Override
    public String propertyName() {
        return getBase().propertyName();
    }

    @Override
    public PropertyListeners events() {
        return listeners;
    }

    @Override
    public UserObjects userObjects() {
        return getBase().userObjects();
    }

    private class DelegatePropertyListener implements PropertyListener {

        @Override
        public void propertyUpdated(PropertyEvent event) {
            listeners.firePropertyUpdated(event);
        }
    }
}
