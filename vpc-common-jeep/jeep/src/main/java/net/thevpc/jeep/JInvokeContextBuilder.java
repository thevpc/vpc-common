package net.thevpc.jeep;

public interface JInvokeContextBuilder {
    JTypedValue getInstance();

    JEvaluator getEvaluator();

    JInvokeContextBuilder setEvaluator(JEvaluator value);

    JInvokeContextBuilder setInstance(JTypedValue instance);

    JContext getContext();

    JInvokeContextBuilder setContext(JContext context);

    JEvaluable[] getArguments();

    JInvokeContextBuilder setArguments(JEvaluable[] args);

    String getName();

    JInvokeContextBuilder setName(String name);

    JCallerInfo getCallerInfo();

    JInvokeContextBuilder setCallerInfo(JCallerInfo callerInfo);

    JInvokeContext build();
}
