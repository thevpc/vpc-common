package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

public abstract class PropertyBase implements Property {

    private final String name;
    private final PropertyType type;
    private final UserObjects userObjects = new DefaultUserObjects();
    protected PropertyListeners listeners;
    protected Property parentProperty;

    public PropertyBase(String name) {
        this(name, (PropertyType) null);
    }

    public PropertyBase(String name, Class type) {
        this(name, PropertyType.of(type));
    }

    public PropertyBase(String name, PropertyType type) {
        this.name = name;
        this.type = type == null ? PropertyType.of(getClass()) : type;
        this.listeners = createListeners();
    }

    protected PropertyListeners createListeners() {
        return new DefaultPropertyListeners(this);
    }

    @Override
    public String propertyName() {
        return name;
    }

    @Override
    public PropertyListeners events() {
        return listeners;
    }

    @Override
    public PropertyType propertyType() {
        return type;
    }

    @Override
    public UserObjects userObjects() {
        return userObjects;
    }

    //    @Override
    @Override
    public String toString() {
        String p = isWritable() ? "Writable" : "ReadOnly";
        return p + "Property(" + fullPropertyName() + "){"
                + "type=" + type
                + '}';
    }

    protected void propagateEvents(Property... others) {
        for (Property other : others) {
            if (other instanceof PropertyBase) {
                ((PropertyBase) other).parentProperty = this;
            }
            events().addDelegate(other, () -> Path.of(propertyName()));
        }
    }

    public String fullPropertyName() {
        String n = "";
        if (parentProperty != null) {
            n = parentProperty.propertyName() + ".";
        }
        return n + name;
    }

    public Property parentProperty() {
        return parentProperty;
    }
}
