package net.thevpc.common.props;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface PropertyListeners extends Iterable<PropertyListener> {

    void addPropagatedInit(PropertyListener run);
    void addPropagatedInit(Runnable run);

    void addInit(Runnable run);

    void addPropagated(PropertyListener listener);
    void addPropagated(Runnable listener);

    void add(Runnable listener);
    void add(PropertyListener listener);

    void remove(Runnable listener);

    void remove(PropertyListener listener);

    void removeIf(Predicate<PropertyListener> predicate);

    PropertyListener[] getAll();

    void addDelegate(Property withListeners, Supplier<Path> pathNameSupplier);

    void removeDelegate(Property listeners);

    void addInit(PropertyListener listener);

    void clear();
}
