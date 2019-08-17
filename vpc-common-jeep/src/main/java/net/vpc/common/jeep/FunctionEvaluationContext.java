package net.vpc.common.jeep;

public interface FunctionEvaluationContext {
    ExpressionEvaluator getExpressionEvaluator();
    ExpressionManager getEvaluator();
    ExpressionNode[] getArguments();
    String getFunctionName();
    Class[] getArgumentTypes();
    Class getReturnType();
}
