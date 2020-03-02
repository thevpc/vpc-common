package net.vpc.common.jeep;

import java.util.Arrays;

public abstract class FunctionBase implements Function {

    private String name;
    private Class resultType;
    private Class[] argTypes;
    private boolean varArgs;

    public FunctionBase(String name, Class returnType, Class[] argTypes) {
        this(name, returnType, argTypes,false);
    }

    public FunctionBase(String name, Class returnType, Class[] argTypes, boolean varArgs) {
        this.resultType = returnType;
        this.name = name;
        this.argTypes = argTypes;
        this.varArgs = varArgs;
        if (varArgs && (this.argTypes.length == 0 || !this.argTypes[this.argTypes.length - 1].isArray())) {
            throw new IllegalArgumentException("Invalid varargs");
        }
    }

    @Override
    public Class getResultType(ExpressionManager evaluator, ExpressionNode[] args) {
        return resultType;
    }

    @Override
    public Class getEffectiveResultType(ExpressionEvaluator evaluator, ExpressionNode[] args) {
        return getResultType(evaluator.getExpressionManager(),args);
    }

    @Override
    public boolean isVarArgs() {
        return varArgs;
    }

    public String getName() {
        return name;
    }

    public Class[] getArgTypes() {
        return Arrays.copyOf(argTypes, argTypes.length);
    }

    public abstract Object eval(ExpressionNode[] args, ExpressionEvaluator evaluator);


    @Override
    public String toString() {
        String n = getName();
        StringBuilder sb = new StringBuilder(n).append("(");
        for (int i = 0; i < argTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String sargi = JeepUtils.getSimpleClassName(argTypes[i]);
            sb.append(sargi);
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public MethodSignature getSignature() {
        return new MethodSignature(getName(),getArgTypes(),isVarArgs());
    }
}
