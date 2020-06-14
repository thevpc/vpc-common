package net.vpc.common.jeep;

public interface JConverter {
    JTypeOrLambda originalType();
    JTypeOrLambda targetType();

    double weight();
    Object convert(Object value, JInvokeContext context);
}
