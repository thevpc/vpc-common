package net.vpc.common.jeep;

public interface JInvokeContextBuilder {
    JTypedValue instance();

    JEvaluator evaluator();

    JInvokeContextBuilder evaluator(JEvaluator value);

    JInvokeContextBuilder instance(JTypedValue instance);

    JContext context();

    JInvokeContextBuilder context(JContext context);

    JEvaluable[] arguments();

    JInvokeContextBuilder arguments(JEvaluable[] args);

    String name();

    JInvokeContextBuilder name(String name);

    JInvokeContext build();
}
