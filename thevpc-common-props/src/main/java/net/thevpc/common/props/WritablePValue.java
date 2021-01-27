package net.thevpc.common.props;

import java.util.function.Function;

public interface WritablePValue<T> extends PValue<T> {

    void set(T v);

    default void setAndBind(WritablePValue<T> other) {
        set(other.get());
        bind(other);
    }

    <T2> void setAndBindConvert(WritablePValue<T2> other, Function<T, T2> map, Function<T2, T> mapBack);


}
