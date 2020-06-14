package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;

public class DefaultJInvokeContextBuilder implements JInvokeContextBuilder {
    private JEvaluator evaluator;
    private JTypedValue instance;
    private JContext context;
    private JEvaluable[] arguments;
    private String name;
    public DefaultJInvokeContextBuilder() {
    }
    public DefaultJInvokeContextBuilder(JContext context, JEvaluator evaluator, JTypedValue instance, JEvaluable[] arguments, String name) {
        this.context = context;
        this.instance = instance;
        this.evaluator = evaluator;
        this.arguments = arguments;
        this.name = name;
    }

    public DefaultJInvokeContextBuilder(JInvokeContextBuilder other) {
        set(other);
    }

    public DefaultJInvokeContextBuilder(JInvokeContext other) {
        set(other);
    }

    public JInvokeContextBuilder set(JInvokeContextBuilder other){
        if(other!=null){
            evaluator(other.evaluator());
            context(other.context());
            instance(other.instance());
            arguments(other.arguments());
            name(other.name());
        }
        return this;
    }
    public JInvokeContextBuilder set(JInvokeContext other){
        if(other!=null){
            evaluator(other.evaluator());
            context(other.context());
            instance(other.instance());
            arguments(other.arguments());
            name(other.name());
        }
        return this;
    }


    @Override
    public JTypedValue instance() {
        return instance;
    }

    @Override
    public JEvaluator evaluator() {
        return evaluator;
    }

    @Override
    public JContext context() {
        return context;
    }

    @Override
    public JEvaluable[] arguments() {
        return arguments;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DefaultJInvokeContextBuilder evaluator(JEvaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public DefaultJInvokeContextBuilder instance(JTypedValue instance) {
        this.instance = instance;
        return this;
    }

    public DefaultJInvokeContextBuilder context(JContext context) {
        this.context = context;
        return this;
    }

    public DefaultJInvokeContextBuilder arguments(JEvaluable[] arguments) {
        this.arguments = arguments;
        return this;
    }

    public DefaultJInvokeContextBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public JInvokeContext build() {
        return new DefaultJInvokeContext(context, evaluator, instance, arguments, name);
    }
}
