package net.thevpc.common.props;

import net.thevpc.common.props.impl.MappedValueBase;
import net.thevpc.common.props.impl.MappedValueBoolean;
import net.thevpc.common.props.impl.WritableValueHelper;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ObservableValue<T> extends Property, GetValueModel<T> {

    default T getRequired() {
        T v = get();
        if (v == null) {
            throw new IllegalArgumentException("required " + propertyName() + " is null");
        }
        return v;
    }

    default T getOrDefault(Supplier<T> t) {
        T v = get();
        if (v == null) {
            return t == null ? null : t.get();
        }
        return v;
    }

    T get();

    ObservableValue<T> readOnly();

    default <V> V getOr(Function<T, V> t) {
        return t.apply(get());
    }

    default void withValue(Consumer<T> t) {
        t.accept(get());
    }

    default void doNonNull(Consumer<T> t) {
        T v = get();
        if (v != null) {
            t.accept(v);
        }
    }


    default void bindTarget(SetValueModel<T> target) {
        WritableValueHelper.helperBindTarget(this, target);
    }

    default <T2> void unbind(SetValueModel<T2> other) {
        WritableValueHelper.helperRemoveBindListeners(events(), other);
    }

    default <T2> void bindConvert(WritableValue<T2> other, Function<T, T2> map) {
        WritableValueHelper.helperBindConvert(this, other, map);
    }

    default <X> ObservableValue<X> mapValue(Class<X> toType, Function<T, X> mapper) {
        return mapValue(PropertyType.of(toType), mapper);
    }

    default ObservableBoolean isNotNull() {
        return mapBooleanValue(x -> x != null);
    }

    default ObservableBoolean isNull() {
        return mapBooleanValue(x -> x == null);
    }

    default ObservableBoolean mapBooleanValue(Function<T, Boolean> mapper) {
        return new MappedValueBoolean<>(propertyName(), this, mapper);
    }

    default ObservableBoolean mapEqualsValue(T compareTo) {
        return mapBooleanValue(x -> Objects.equals(x, compareTo));
    }

    default ObservableBoolean mapNotEqualsValue(T compareTo) {
        return mapBooleanValue(x -> !Objects.equals(x, compareTo));
    }

    default <X> ObservableValue<Integer> mapIntValue(Function<T, Integer> mapper) {
        return mapValue(PropertyType.of(Integer.class), mapper);
    }

    default <X> ObservableValue<Long> mapLongValue(Function<T, Long> mapper) {
        return mapValue(PropertyType.of(Long.class), mapper);
    }

    default <X> ObservableValue<Double> mapDoubleValue(Function<T, Double> mapper) {
        return mapValue(PropertyType.of(Double.class), mapper);
    }

    default <X> ObservableValue<Float> mapFloatValue(Function<T, Float> mapper) {
        return mapValue(PropertyType.of(Float.class), mapper);
    }

    default <X> ObservableValue<String> mapStringValue(Function<T, String> mapper) {
        return mapValue(PropertyType.of(String.class), mapper);
    }

    default <X> ObservableValue<X> mapValue(PropertyType toType, Function<T, X> mapper) {
        return new MappedValueBase<T, X>(propertyName(), toType, this, mapper);
    }

    default Class<T> propertyClass() {
        return propertyType().getTypeClass();
    }

}
