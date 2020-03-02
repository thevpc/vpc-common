package net.vpc.common.prpbind;

import net.vpc.common.prpbind.impl.WritablePValueImpl;

public interface WritablePValue<T> extends PValue<T> {
    void set(T v);

    PValue<T> readOnly();

    PropertyVetos vetos();
}
