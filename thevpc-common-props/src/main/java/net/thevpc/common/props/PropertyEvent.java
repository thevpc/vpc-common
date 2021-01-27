package net.thevpc.common.props;

import net.thevpc.common.props.impl.PrpBindUtils;

public class PropertyEvent {

    private Property property;
    private Object index;
    private Object oldValue;
    private Object newValue;
    private String path;
    private PropertyUpdate action;

    public PropertyEvent(Property property, Object index, Object oldValue, Object newValue, String path, PropertyUpdate action) {
        this.property = property;
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.path = path;
        this.action = action;
    }

    public PropertyUpdate getAction() {
        return action;
    }

    public int getPathLength() {
        return getPathArray().length;
    }

    public String[] getPathArray() {
        return PrpBindUtils.splitPath(path);
    }

    public String getPath() {
        return path;
    }

    public <T extends Property> T getProperty() {
        return (T) property;
    }

    public <T> T getIndex() {
        return (T) index;
    }

    public <T> T getOldValue() {
        return (T) oldValue;
    }

    public <T> T getNewValue() {
        return (T) newValue;
    }

    @Override
    public String toString() {
        return String.valueOf(action) + "{"
                + " path='" + path + '\''
                + ", property=" + property
                + ", oldValue=" + oldValue
                + ", newValue=" + newValue
                + (index == null ? "" : (", index=" + index))
                + '}';
    }
}
