package net.thevpc.common.props.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.thevpc.common.props.*;
import net.thevpc.common.props.*;

public class PropertyListenersImpl implements PropertyListeners {

    protected List<PropertyListener> listeners;
    protected List<WithListeners> delegates;
    protected Object source;

    public PropertyListenersImpl(Object source) {
        this.source = source;
    }

    @Override
    public void add(PropertyListener listener) {
        if (listener != null) {
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
                listeners.sort((PropertyListener o1, PropertyListener o2) -> Integer.compare(o1.order(), o2.order()));
            }
        }
    }

    @Override
    public void remove(PropertyListener listener) {
        if (listener != null) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    @Override
    public void removeIf(Predicate<PropertyListener> predicate) {
        for (PropertyListener p : getAll()) {
            if (predicate.test(p)) {
                remove(p);
            }
        }
    }

    public PropertyListener[] getAll() {
        return listeners == null ? new PropertyListener[0] : listeners.toArray(new PropertyListener[0]);
    }

    public void addDelegate(Property p) {
        addDelegate(p, () -> p.name());
    }

    public void addDelegate(WithListeners withListeners, Supplier<String> pathNameSupplier) {
        if (delegates == null) {
            delegates = new ArrayList<>();
        }
        System.out.println(source + " :: addDelegate(" + withListeners + ")");
        delegates.add(withListeners);
        withListeners.listeners().add(new NamedPropertyListener(this, pathNameSupplier));
    }

    public void removeDelegate(WithListeners listeners) {
        if (delegates != null) {
            delegates.add(listeners);
            listeners.listeners().removeIf(x -> x instanceof NamedPropertyListener && ((NamedPropertyListener) x).owner == this);
        }
    }

    @Override
    public Iterator<PropertyListener> iterator() {
        return Arrays.asList(getAll()).iterator();
    }

    public void firePropertyUpdated(PropertyEvent event) {
        if (listeners != null) {
            for (PropertyListener listener : getAll()) {
                listener.propertyUpdated(event);
            }
        }
    }

    private static class NamedPropertyListener implements PropertyListener {

        private PropertyListenersImpl owner;
        private Supplier<String> pathName;

        public NamedPropertyListener(PropertyListenersImpl owner, Supplier<String> pathName) {
            this.owner = owner;
            this.pathName = pathName;
        }

        public void propertyUpdated(PropertyEvent event) {
            if (owner.listeners != null) {
                String s1 = pathName.get();
                event = PropsHelper.prefixPath(event, s1);
//                if (event.getOldValue() instanceof WithListeners) {
//                    owner.removeDelegate(event.getOldValue());
//                }
//                if (event.getNewValue() instanceof WithListeners) {
//                    owner.addDelegate(event.getNewValue(), pathName);
//                }
                owner.firePropertyUpdated(event);
            }
        }
    }

}
