package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JTypedValue;

public class DefaultJTypedValue implements JTypedValue {
    private Object value;
    private JType type;

    public DefaultJTypedValue(Object value, JType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JType getType() {
        return type;
    }
}
