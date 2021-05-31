package net.thevpc.common.props;

import net.thevpc.common.props.impl.MappedValueBase;
import net.thevpc.common.props.impl.WritableValueHelper;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ObservableBoolean extends ObservableValue<Boolean> {
    ObservableBoolean readOnly();
    default ObservableBoolean not() {
        return mapBooleanValue(x->!x);
    }

}
