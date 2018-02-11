package net.vpc.common.strings;

/**
 * Created by vpc on 4/16/17.
 */
public class ExprVar implements Expr {
    private String name;

    public ExprVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
        return evaluator.evalVar(context, this);
    }

    @Override
    public String toString() {
        return name;
    }
}
