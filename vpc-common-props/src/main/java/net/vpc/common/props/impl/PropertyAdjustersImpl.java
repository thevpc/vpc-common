package net.vpc.common.props.impl;

import net.vpc.common.props.Property;
import net.vpc.common.props.PropertyAdjuster;
import net.vpc.common.props.PropertyAdjusters;
import net.vpc.common.props.PropertyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.vpc.common.props.*;

public class PropertyAdjustersImpl implements PropertyAdjusters {
    protected Property source;
    protected List<PropertyAdjuster> listeners;
    protected boolean readOnly;

    public PropertyAdjustersImpl(Property source) {
        this.source = source;
    }

    public void readOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean readOnly() {
        return this.readOnly;
    }

    public void add(PropertyAdjuster listener) {
        if (readOnly) {
            throw new IllegalArgumentException("Read ONly");
        }
        if (listener != null) {
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    public void remove(PropertyAdjuster listener) {
        if (readOnly) {
            throw new IllegalArgumentException("Read ONly");
        }
        if (listener != null) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    public PropertyAdjuster[] getAll() {
        return listeners == null ? new PropertyAdjuster[0] : listeners.toArray(new PropertyAdjuster[0]);
    }

    public PropertyEvent firePropertyUpdated(PropertyEvent event) {
        if (listeners != null) {
            for (PropertyAdjuster listener : listeners) {
                Object v2 = listener.adjustNewValue(event);
                if (Objects.equals(event.getOldValue(), v2)) {
                    return null;
                }
                event = new PropertyEvent(event.getProperty(), event.getIndex(), event.getOldValue(), v2, event.getPath(), event.getAction());
            }
        }
        return event;
    }

}
