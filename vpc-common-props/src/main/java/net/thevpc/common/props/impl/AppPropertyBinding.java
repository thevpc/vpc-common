package net.thevpc.common.props.impl;

import net.thevpc.common.props.Property;
import net.thevpc.common.props.*;

public class AppPropertyBinding {
    private String path;
    private Property property;

    public AppPropertyBinding(Property property, String path) {
        this.path = path;
        this.property = property;
    }

    public String getPath() {
        return path;
    }

    public Property getProperty() {
        return property;
    }
}
