/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionNode;
import net.vpc.common.jeep.ExpressionEvaluator;
import net.vpc.common.jeep.JeepPlatformUtils;

/**
 *
 * @author vpc
 */
public class ExpressionNodeIf extends StatementNode {

    private ExpressionNode condition;
    private ExpressionNode trueBlock;
    private ExpressionNode falseBlock;

    public ExpressionNodeIf() {
        super(null);
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public ExpressionNode getTrueBlock() {
        return trueBlock;
    }

    public void setTrueBlock(ExpressionNode trueBlock) {
        this.trueBlock = trueBlock;
    }

    public ExpressionNode getFalseBlock() {
        return falseBlock;
    }

    public void setFalseBlock(ExpressionNode falseBlock) {
        this.falseBlock = falseBlock;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        Class c1 = trueBlock == null ? Void.TYPE : trueBlock.getExprType(evaluator);
        Class c2 = falseBlock == null ? Void.TYPE : falseBlock.getExprType(evaluator);
        return JeepPlatformUtils.firstCommonSuperType(c1, c2);
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        boolean ok = evalBooleanExpression(condition, evaluator);
        evaluator.getExpressionManager().debug("IF " + condition + " == " + ok);
        Object o = null;
        if (ok) {
            if (trueBlock != null) {
                o = trueBlock.evaluate(evaluator);
            }
        } else {
            if (falseBlock != null) {
                o = falseBlock.evaluate(evaluator);
            }
        }
        return o;
    }

    public static boolean evalBooleanExpression(ExpressionNode condition, ExpressionEvaluator evaluator) {
        boolean ok = false;
        if (condition == null) {
            ok = true;
        } else {
            Object u = condition.evaluate(evaluator);
            if (u instanceof ExpressionNodeLiteral) {
                u = ((ExpressionNodeLiteral) u).getValue();
            }
            if (u instanceof Boolean) {
                ok = ((Boolean) u).booleanValue();
            }
        }
        return ok;
    }

    @Override
    public String toString(String prefix) {
        StringBuilder sb = new StringBuilder(prefix).append("if ");
        sb.append(condition).append(NEWLINE);
        sb.append(trueBlock.toString(prefix + TAB)).append(NEWLINE);
        if (falseBlock != null) {
            sb.append(prefix).append("else").append(NEWLINE);
            sb.append(falseBlock.toString(prefix + TAB)).append(NEWLINE);
        }
        sb.append(prefix).append("end");
        return sb.toString();
    }

}
