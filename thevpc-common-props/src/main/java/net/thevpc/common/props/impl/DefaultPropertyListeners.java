package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DefaultPropertyListeners implements PropertyListeners {

    protected List<PropertyListener> listeners;
    protected List<Property> delegates;
    protected Object source;

    public DefaultPropertyListeners(Object source) {
        this.source = source;
    }

    public void initSource(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    @Override
    public void addPropagatedInit(Runnable listener) {
        if (listener == null) {
            return;
        }
        addPropagatedInit(new RunnableToPropertyListener(listener));
    }

    @Override
    public void addInit(Runnable listener) {
        if (listener == null) {
            return;
        }
        addInit(new RunnableToPropertyListener(listener));
    }

    public void addPropagated(PropertyListener listener) {
        if (listener == null) {
            return;
        }
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
                listeners.sort(Comparator.comparingInt(PropertyListener::order));
            }
        }
    }

    @Override
    public void addPropagated(Runnable listener) {
        if (listener == null) {
            return;
        }
        addPropagated(new RunnableToPropertyListener(listener));
    }

    @Override
    public void add(Runnable listener) {
        if (listener == null) {
            return;
        }
        add(new RunnableToPropertyListener(listener));
    }

    @Override
    public void add(PropertyListener listener) {
        if (listener == null) {
            return;
        }
        addPropagated(new ImmediatePropertyListener(listener));
    }

    @Override
    public void remove(Runnable listener) {
        if (listener == null) {
            return;
        }
        remove(new RunnableToPropertyListener(listener));
    }

    @Override
    public void remove(PropertyListener listener) {
        if (listener == null) {
            return;
        }
        if (listeners != null) {
            synchronized (listeners) {
                for (Iterator<PropertyListener> iterator = listeners.iterator(); iterator.hasNext(); ) {
                    PropertyListener o = iterator.next();
                    if (o == listener) {
                        iterator.remove();
                        break;
                    } else if (o instanceof ImmediatePropertyListener && ((ImmediatePropertyListener) o).listener == listener) {
                        iterator.remove();
                        break;
                    }
                }
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
        if (listeners == null) {
            return new PropertyListener[0];
        }
        synchronized (listeners) {
            return listeners == null ? new PropertyListener[0] : listeners.toArray(new PropertyListener[0]);
        }
    }

    @Override
    public void addDelegate(Property prop, Supplier<Path> pathNameSupplier) {
        if (prop == null) {
            throw new NullPointerException();
        }
        if (delegates == null) {
            delegates = new ArrayList<>();
        }
        if (!delegates.contains(prop)) {
            delegates.add(prop);
            prop.events().addPropagated(new NamedPropertyListener(this, pathNameSupplier));
        }
    }

    @Override
    public void removeDelegate(Property property) {
        if (property == null) {
            return;
        }
        if (delegates != null) {
            delegates.add(property);
            property.events().removeIf(x -> x instanceof NamedPropertyListener && ((NamedPropertyListener) x).owner == this);
        }
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    @Override
    public void addInit(PropertyListener listener) {
        if (listener == null) {
            return;
        }
        PropertyEvent e = new PropertyEvent(
                (Property) getSource(),
                null, null, null, Path.root(), PropertyUpdate.INIT, null, true
        );
        listener.propertyUpdated(e);
        add(listener);
    }
    public void addPropagatedInit(PropertyListener listener) {
        if (listener == null) {
            return;
        }
        PropertyEvent e = new PropertyEvent(
                (Property) getSource(),
                null, null, null, Path.root(), PropertyUpdate.INIT, null, true
        );
        listener.propertyUpdated(e);
        addPropagated(listener);
    }

    public void addDelegate(Property p) {
        addDelegate(p, () -> Path.of(p.propertyName()));
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

        private DefaultPropertyListeners owner;
        private Supplier<Path> pathName;

        public NamedPropertyListener(DefaultPropertyListeners owner, Supplier<Path> pathName) {
            this.owner = owner;
            this.pathName = pathName;
        }

        public void propertyUpdated(PropertyEvent event) {
            if (owner.listeners != null) {
                Path s1 = pathName.get();
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

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + Objects.hashCode(this.owner);
            hash = 59 * hash + Objects.hashCode(this.pathName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NamedPropertyListener other = (NamedPropertyListener) obj;
            if (!Objects.equals(this.owner, other.owner)) {
                return false;
            }
            if (!Objects.equals(this.pathName, other.pathName)) {
                return false;
            }
            return true;
        }

    }

    private static class ImmediatePropertyListener implements PropertyListener {

        private final PropertyListener listener;

        public ImmediatePropertyListener(PropertyListener listener) {
            this.listener = listener;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            if (event.immediate()) {
                listener.propertyUpdated(event);
            }
        }
    }

    public static class RunnableToPropertyListener implements PropertyListener {
        private final Runnable t;

        public RunnableToPropertyListener(Runnable t) {
            this.t = t;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            t.run();
        }

        @Override
        public int hashCode() {
            return Objects.hash(t);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RunnableToPropertyListener that = (RunnableToPropertyListener) o;
            return Objects.equals(t, that.t);
        }
    }
}
