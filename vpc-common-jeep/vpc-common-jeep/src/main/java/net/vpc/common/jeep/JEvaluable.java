package net.vpc.common.jeep;

public interface JEvaluable {
    JType type();
    Object evaluate(JInvokeContext context);
}

