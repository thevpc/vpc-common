package net.vpc.common.jeep;

public abstract class AbstractExpressionEvaluatorResolver implements ExpressionEvaluatorResolver {
    @Override
    public Variable resolveVariable(String name, ExpressionManager context) {
        return null;
    }

    @Override
    public Function resolveFunction(String name, ExpressionNode[] args, ArgsPossibility argPossibility, ExpressionManager context) {
        return null;
    }

    @Override
    public Object implicitConvertLiteral(Object literal, ExpressionManager evaluator) {
        return null;
    }

    @Override
    public ExpressionEvaluatorConverter[] resolveImplicitConverters(Class type) {
        return new ExpressionEvaluatorConverter[0];
    }
}
