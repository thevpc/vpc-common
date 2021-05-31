package net.thevpc.common.props.impl;

import net.thevpc.common.props.Property;
import net.thevpc.common.props.PropertyAdjuster;
import net.thevpc.common.props.PropertyAdjusters;
import net.thevpc.common.props.PropertyEvent;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdjustersReadOnly implements PropertyAdjusters {
    protected Property source;
    protected boolean readOnly;

    public PropertyAdjustersReadOnly(Property source) {
        this.source = source;
    }

    public void readOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean readOnly() {
        return this.readOnly;
    }

    public void add(PropertyAdjuster listener) {
        throw new IllegalArgumentException("Read Only");
    }

    public void remove(PropertyAdjuster listener) {
        throw new IllegalArgumentException("Read Only");
    }

    public PropertyAdjuster[] getAll() {
        return new PropertyAdjuster[0];
    }
}
