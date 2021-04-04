package net.thevpc.common.props;

import java.util.function.Predicate;

public interface PropertyListeners extends Iterable<PropertyListener>{
    
    void add(PropertyListener listener);

    void remove(PropertyListener listener);

    void removeIf(Predicate<PropertyListener> predicate);

    PropertyListener[] getAll();
}
