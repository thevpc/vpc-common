package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

public abstract class WritablePropertyBase extends PropertyBase implements WritableProperty {

    protected PropertyVetosImpl vetos = new PropertyVetosImpl(this);
    protected PropertyAdjustersImpl adjusters = new PropertyAdjustersImpl(this);
    public WritablePropertyBase(String name, PropertyType type) {
        super(name,type);
    }

    //    @Override
    @Override
    public PropertyAdjusters adjusters() {
        return adjusters;
    }

    public PropertyVetos vetos() {
        return vetos;
    }

}
