package net.vpc.common.strings;

import java.util.Arrays;

/**
 * Created by vpc on 4/16/17.
 */
public class ExprOperator implements Expr {
    private String userName;
    private ExpressionParser.OperatorDef operatorDef;
    private Expr[] operands;

    public ExprOperator(String userName, ExpressionParser.OperatorDef operatorDef, Expr[] operands) {
        this.userName = userName;
        this.operatorDef = operatorDef;
        this.operands = operands;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return operatorDef.getName();
    }

    public int getPrecedence() {
        return operatorDef.getPrecedence();
    }

    public ExpressionParser.OperatorDef getOperatorDef() {
        return operatorDef;
    }

    public Expr getOperand(int index) {
        if(index>=operands.length){
            throw new ArrayIndexOutOfBoundsException("invalid operand index "+index+" for operator "+userName);
        }
        return operands[index];
    }

    public Expr[] getOperands() {
        return operands;
    }

    @Override
    public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
        return evaluator.evalOperator(context, this);
    }

    @Override
    public String toString() {
        if (operatorDef instanceof ExpressionParser.BinaryOperatorDef) {
            return "(" + operands[0].toString() + operatorDef.getName() + operands[1].toString() + ")";
        }
        return "ExprOperator{" +
                "operatorDef=" + operatorDef +
                ", operands=" + Arrays.toString(operands) +
                '}';
    }
}
