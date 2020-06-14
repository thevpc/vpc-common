package net.vpc.common.jeep.impl.eval;

import net.vpc.common.jeep.*;

public class JEvaluableConverter implements JEvaluable {
    private final JConverter argConverter;
    private final JEvaluable value;

    public JEvaluableConverter(JConverter argConverter, JEvaluable value) {
        this.argConverter = argConverter;
        this.value = value;
    }

    @Override
    public JType type() {
        return argConverter.targetType().getType();
    }

    @Override
    public Object evaluate(JInvokeContext context) {
        return argConverter.convert(value.evaluate(context), context);
    }

    public JConverter getArgConverter() {
        return argConverter;
    }

    public JEvaluable getValue() {
        return value;
    }
}
