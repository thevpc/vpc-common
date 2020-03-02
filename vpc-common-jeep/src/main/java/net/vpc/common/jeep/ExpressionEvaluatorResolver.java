package net.vpc.common.jeep;

public interface ExpressionEvaluatorResolver {

    Variable resolveVariable(String name, ExpressionManager context) ;

    Function resolveFunction(String name, ExpressionNode[] args, ArgsPossibility argPossibility, ExpressionManager context);

    Object implicitConvertLiteral(Object literal, ExpressionManager evaluator);

    ExpressionEvaluatorConverter[] resolveImplicitConverters(Class type);

}
