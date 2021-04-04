package net.thevpc.common.props;

import java.util.function.Function;

public interface WritableValue<T> extends ObservableValue<T> {

    void set(T v);

    default void setAndBind(WritableValue<T> other) {
        set(other.get());
        bind(other);
    }

    <T2> void setAndBindConvert(WritableValue<T2> other, Function<T, T2> map, Function<T2, T> mapBack);


}
