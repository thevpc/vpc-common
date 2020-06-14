package net.vpc.common.jeep;

public interface JInvokeContext {
    JEvaluator evaluator();

    JTypedValue instance();

    Object evaluateArg(int index);

    Object evaluate(JNode node);

    Object evaluate(JEvaluable node);

    Object[] evaluate(JEvaluable[] node);

    JContext context();

    JEvaluable[] arguments();

    String name();

    JType[] argumentTypes();

    JInvokeContextBuilder builder();
}
