package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;

public class DefaultJInvokeContext implements JInvokeContext {
    private JEvaluator evaluator;
    private JTypedValue instance;
    private JContext context;
    private JEvaluable[] arguments;
    private String name;
    private JType[] argumentTypes;

    public DefaultJInvokeContext(JContext context, JEvaluator evaluator, JTypedValue instance, JEvaluable[] arguments, String name) {
        this.context = context;
        this.instance = instance;
        this.evaluator = evaluator;
        this.arguments = arguments;
        this.name = name;
        this.argumentTypes = new JType[arguments.length];
        for (int i = 0; i < this.argumentTypes.length; i++) {
            this.argumentTypes[i]=arguments[i].type();
        }
    }

    @Override
    public Object evaluateArg(int index) {
        return evaluate(arguments[index]);
    }

    @Override
    public JEvaluator evaluator() {
        return evaluator;
    }

    @Override
    public JTypedValue instance() {
        return instance;
    }

    @Override
    public Object evaluate(JEvaluable node) {
        return node.evaluate(this);
    }

    @Override
    public Object evaluate(JNode node) {
        return evaluator().evaluate(node,this);
    }

    @Override
    public Object[] evaluate(JEvaluable[] node) {
        Object[] a=new Object[node.length];
        for (int i = 0; i < a.length; i++) {
            a[i]=evaluate(node[i]);
        }
        return a;
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
    public JType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public JInvokeContextBuilder builder() {
        return new DefaultJInvokeContextBuilder(this);
    }
}
