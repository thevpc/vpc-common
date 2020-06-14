package net.vpc.common.props.impl;
import net.vpc.common.props.*;

public class WritablePValueImpl<T> extends WritablePValueBase<T> implements WritablePValue<T> {

    public WritablePValueImpl(String name, PropertyType type, T value) {
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
    public PValue<T> readOnly() {
        return super.readOnly();
    }

    @Override
    protected PValue<T> createReadOnly() {
        return new ReadOnlyPValue<>(this);
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }
}
