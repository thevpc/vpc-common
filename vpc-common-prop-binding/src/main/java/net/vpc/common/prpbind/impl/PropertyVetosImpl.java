package net.vpc.common.prpbind.impl;

import net.vpc.common.prpbind.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PropertyVetosImpl implements PropertyVetos {
    protected Property source;
    protected List<PropertyVeto> vetos;

    public PropertyVetosImpl(Property source) {
        this.source = source;
    }

    public void add(PropertyVeto listener) {
        if (listener != null) {
            if (vetos == null) {
                vetos = new ArrayList<>();
            }
            if (!vetos.contains(listener)) {
                vetos.add(listener);
            }
        }
    }

    public void remove(PropertyVeto listener) {
        if (listener != null) {
            if (vetos != null) {
                vetos.remove(listener);
            }
        }
    }

    public PropertyVeto[] getAll() {
        return vetos == null ? new PropertyVeto[0] : vetos.toArray(new PropertyVeto[0]);
    }

    public void firePropertyUpdated(PropertyEvent event) {
        if (vetos != null) {
            for (PropertyVeto listener : vetos) {
                listener.vetoableChange(event);
            }
        }
    }

    public void firePropertyUpdated(Object old, Object v, Object index, PropertyUpdate action) {
        if (vetos != null) {
            if (!Objects.equals(old, v)) {
                PropertyEvent event = null;
                for (PropertyVeto listener : vetos) {
                    if (event == null) {
                        event = new PropertyEvent(source, index, old, v, "/", action);
                    }
                    listener.vetoableChange(event);
                }
            }
        }
    }


}
