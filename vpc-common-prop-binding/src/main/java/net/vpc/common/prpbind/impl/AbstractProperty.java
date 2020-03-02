package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

public abstract class AbstractProperty implements Property {
    protected PropertyVetosImpl vetos = new PropertyVetosImpl(this);
    protected PropertyAdjustersImpl adjusters = new PropertyAdjustersImpl(this);
    protected PropertyListenersImpl listeners = new PropertyListenersImpl(this);
    private String name;
    private PropertyType type;

    public AbstractProperty(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }

    public String getPropertyName() {
        return name;
    }

    @Override
    public PropertyType getType() {
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
        return "Property{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
