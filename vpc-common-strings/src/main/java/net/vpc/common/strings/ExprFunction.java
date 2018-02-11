package net.vpc.common.strings;

/**
 * Created by vpc on 4/16/17.
 */
public class ExprFunction extends ExprOperator {
    private ExpressionParser.OperatorDefFunction functionDef;

    public ExprFunction(ExpressionParser.OperatorDefFunction functionDef, Expr[] parameters) {//
        super(functionDef.getDef().getName(), new ExpressionParser.OperatorDefFct(functionDef), parameters);
        this.functionDef = functionDef;
    }

    public String getName() {
        return functionDef.getUserName();
    }

    public ExpressionParser.FunctionDef getFunctionDef() {
        return functionDef.getDef();
    }

    public Expr getParameter(int index) {
        return getOperand(index);
    }

    public Expr[] getParameters() {
        return getOperands();
    }

    @Override
    public ExpressionParser.OperatorDefFct getOperatorDef() {
        return (ExpressionParser.OperatorDefFct) super.getOperatorDef();
    }

    @Override
    public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
        return evaluator.evalFunction(context, this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getName()).append("(");
        int length = getOperands().length;
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getOperand(i));
        }
        sb.append(")");
        return sb.toString();
    }
}
