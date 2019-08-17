package net.vpc.common.jeep;

public class FunctionForHandler extends FunctionBase {
    private FunctionHandler handler;

    public FunctionForHandler(String name, Class returnType, Class[] argTypes, boolean varArgs,FunctionHandler handler) {
        super(name, returnType, argTypes,varArgs);
        this.handler = handler;
    }

    @Override
    public Object evaluate(ExpressionNode[] args, ExpressionEvaluator evaluator) {
        return handler.evaluate(new DefaultFunctionEvaluationContext(evaluator, evaluator.getExpressionManager(),args, getName(), getArgTypes(),
                getResultType(evaluator.getExpressionManager(),args)));
    }
}
