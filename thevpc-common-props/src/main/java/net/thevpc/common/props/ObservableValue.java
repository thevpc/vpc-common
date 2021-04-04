package net.thevpc.common.props;

import java.util.function.Function;

public interface ObservableValue<T> extends Property {

    void bind(WritableValue<T> other);

    <T2> void unbind(WritableValue<T2> other);
    
    <T2> void bindConvert(WritableValue<T2> other, Function<T, T2> map);

    T get();

    ObservableValue<T> readOnly();
}
