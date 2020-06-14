package net.vpc.common.props.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.vpc.common.props.*;

public class PropertyVetosImpl implements PropertyVetos {

    protected Property source;
    protected List<PropertyVeto> vetos;

    public PropertyVetosImpl(Property source) {
        this.source = source;
    }

    @Override
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

    @Override
    public void remove(PropertyVeto listener) {
        if (listener != null) {
            if (vetos != null) {
                vetos.remove(listener);
            }
        }
    }

    @Override
    public void removeIf(Predicate<PropertyVeto> predicate) {
        for (PropertyVeto p : getAll()) {
            if (predicate.test(p)) {
                remove(p);
            }
        }
    }

    @Override
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
