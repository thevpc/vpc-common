package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

public class PropertySupport {

    private String name;
    private Object source;
    private PropertyListeners listeners;
    private DefaultUserObjects userObjects;
    private PropertyType propertyType;
    private boolean writable;
    private Path namePath;

    public PropertySupport(String name, Object source, PropertyType propertyType, boolean writable, PropertyListeners listeners) {
        this.propertyType = propertyType == null ? PropertyType.of(source.getClass()) : propertyType;
        this.name = name;
        this.source = source;
        this.listeners = listeners == null ? new DefaultPropertyListeners(source) : listeners;
        this.userObjects = new DefaultUserObjects();
        this.writable = writable;
        this.namePath = Path.of(name());
    }

    public boolean isWritable() {
        return writable;
    }

    public PropertyType type() {
        return propertyType;
    }

    public String name() {
        return name;
    }

    public PropertyListeners listeners() {
        return listeners;
    }

    public UserObjects userObjects() {
        return userObjects;
    }

    public void propagateEvents(Property other) {
        listeners().addDelegate(other, () -> namePath);
    }
}
