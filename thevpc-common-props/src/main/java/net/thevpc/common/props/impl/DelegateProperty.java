package net.thevpc.common.props.impl;
import net.thevpc.common.props.*;

public abstract class DelegateProperty<T> implements Property {

    protected PropertyListenersImpl listeners;
    protected PropertyVetosImpl vetos;
    protected Property base;

    public DelegateProperty(Property base) {
        this.base = base;
        listeners = new PropertyListenersImpl(this);
        vetos = new PropertyVetosImpl(this);
        base.listeners().add(new DelegatePropertyListener());
        base.vetos().add(new DelegatePropertyVeto());
    }

    public Property getBase() {
        return base;
    }

    @Override
    public PropertyType type() {
        return getBase().type();
    }

    @Override
    public boolean isWritable() {
        return getBase().isWritable();
    }

    @Override
    public String name() {
        return getBase().name();
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    @Override
    public PropertyVetos vetos() {
        return vetos;
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

    private class DelegatePropertyVeto implements PropertyVeto {

        @Override
        public void vetoableChange(PropertyEvent event) {
            vetos.firePropertyUpdated(event);
        }
    }
}
