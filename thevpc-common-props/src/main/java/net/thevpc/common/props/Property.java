package net.thevpc.common.props;

import net.thevpc.common.props.impl.WritableValueHelper;

import java.util.Objects;
import java.util.function.Consumer;

public interface Property {

    default Property getChildProperty(Path path) {
        return WritableValueHelper.getChildProperty(this, path);
    }

    default <T> void with(Path path, Consumer<T> t) {
        Property p = getChildProperty(path);
        if (p != null) {
            t.accept((T) p);
        }
    }

    default void onChange(Runnable t) {
        events().add(t);
    }

    default void onPropagatedChange(Runnable t) {
        events().addPropagated(t);
    }

    default void onChangeAndInit(Runnable t) {
        events().addInit(t);
    }

    default void onPropagatedChangeAndInit(Runnable t) {
        events().addPropagatedInit(t);
    }

    default void onChange(PropertyListener t) {
        events().add(t);
    }

    default void onPropagatedChange(PropertyListener t) {
        events().addPropagated(t);
    }

    default void onChangeAndInit(PropertyListener t) {
        events().addInit(t);
    }

    default void onPropagatedChangeAndInit(PropertyListener t) {
        events().addPropagatedInit(t);
    }

    default <V> V with(Consumer<V> t) {
        if (t != null) {
            t.accept((V) this);
        }
        return (V) this;
    }

    String propertyName();

    PropertyListeners events();

    PropertyType propertyType();

    UserObjects userObjects();

    default Property readOnly() {
        throw new UnsupportedOperationException("unsupported readOnly");
    }

    default boolean isWritable() {
        return true;
    }

}
