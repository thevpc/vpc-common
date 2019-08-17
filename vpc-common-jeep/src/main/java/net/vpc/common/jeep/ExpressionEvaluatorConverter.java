package net.vpc.common.jeep;

public interface ExpressionEvaluatorConverter {
    Class getOriginalType();
    Class getTargetType();
    Object convert(Object value);
}
