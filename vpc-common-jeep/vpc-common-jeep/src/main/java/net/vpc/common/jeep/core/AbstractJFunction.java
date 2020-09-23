package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JFunction;
import net.vpc.common.jeep.JTypes;
import net.vpc.common.jeep.impl.functions.AbstractJInvokable;

public abstract class AbstractJFunction extends AbstractJInvokable implements JFunction {
    private JTypes types;

    public AbstractJFunction(JTypes types) {
        this.types = types;
    }

    @Override
    public JTypes getTypes() {
        return types;
    }
}
