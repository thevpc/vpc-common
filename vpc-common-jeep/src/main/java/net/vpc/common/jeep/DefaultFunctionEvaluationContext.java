package net.vpc.common.jeep;

public class DefaultFunctionEvaluationContext implements FunctionEvaluationContext{
    private ExpressionEvaluator expressionEvaluator;
    private ExpressionManager evaluator;
    private ExpressionNode[] arguments;
    private String functionName;
    private Class[] argumentTypes;
    private Class returnType;

    public DefaultFunctionEvaluationContext(ExpressionEvaluator expressionEvaluator, ExpressionManager evaluator, ExpressionNode[] arguments, String functionName, Class[] argumentTypes, Class returnType) {
        this.expressionEvaluator = expressionEvaluator;
        this.evaluator = evaluator;
        this.arguments = arguments;
        this.functionName = functionName;
        this.argumentTypes = argumentTypes;
        this.returnType = returnType;
    }

    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        return expressionEvaluator;
    }

    @Override
    public ExpressionManager getEvaluator() {
        return evaluator;
    }

    @Override
    public ExpressionNode[] getArguments() {
        return arguments;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public Class[] getArgumentTypes() {
        return argumentTypes;
    }

    @Override
    public Class getReturnType() {
        return returnType;
    }
}
