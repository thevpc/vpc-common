package net.vpc.common.prpbind;

import java.util.function.Predicate;

public interface PropertyListeners extends Iterable<PropertyListener>{
    void add(PropertyListener listener);

    void remove(PropertyListener listener);

    void removeIf(Predicate<PropertyListener> predicate);

    PropertyListener[] getAll();
}
