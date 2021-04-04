package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

public abstract class AbstractProperty implements Property {

    protected PropertyVetosImpl vetos = new PropertyVetosImpl(this);
    protected PropertyAdjustersImpl adjusters = new PropertyAdjustersImpl(this);
    protected PropertyListenersImpl listeners = new PropertyListenersImpl(this);
    private final String name;
    private final PropertyType type;
    private final UserObjects userObjects = new UserObjectsImpl();

    public AbstractProperty(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public UserObjects userObjects() {
        return userObjects;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public PropertyType type() {
        return type;
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    //    @Override
    public PropertyAdjusters adjusters() {
        return adjusters;
    }

    public PropertyVetos vetos() {
        return vetos;
    }

    @Override
    public String toString() {
        return "Property{"
                + "name='" + name + '\''
                + ", type=" + type
                + '}';
    }
}
