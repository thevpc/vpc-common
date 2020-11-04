package net.thevpc.common.props;

import java.util.function.Function;

public interface PValue<T> extends Property {

    void bind(WritablePValue<T> other);

    <T2> void unbind(WritablePValue<T2> other);
    
    <T2> void bindConvert(WritablePValue<T2> other, Function<T, T2> map);

    T get();

    PValue<T> readOnly();
}
