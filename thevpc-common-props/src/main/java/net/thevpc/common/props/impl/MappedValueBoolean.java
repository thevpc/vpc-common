package net.thevpc.common.props.impl;

import net.thevpc.common.props.ObservableBoolean;
import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.PropertyType;

import java.util.function.Function;

public class MappedValueBoolean<F> extends MappedValueBase<F,Boolean> implements ObservableBoolean {
    public MappedValueBoolean(String name, ObservableValue<F> value, Function<F, Boolean> mapper) {
        super(name, PropertyType.of(Boolean.class), value, mapper);
    }

    @Override
    public ObservableBoolean readOnly() {
        return this;
    }

}
