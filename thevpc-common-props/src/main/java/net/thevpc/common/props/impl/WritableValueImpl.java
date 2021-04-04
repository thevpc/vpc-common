package net.thevpc.common.props.impl;
import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.ObservableValue;

public class WritableValueImpl<T> extends WritableValueBase<T> implements WritableValue<T> {

    public WritableValueImpl(String name, PropertyType type, T value) {
        super(name, type, value);
        if (type.getArgs().length > 0) {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public ObservableValue<T> readOnly() {
        return super.readOnly();
    }

    @Override
    protected ObservableValue<T> createReadOnly() {
        return new ReadOnlyValue<>(this);
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }
}
