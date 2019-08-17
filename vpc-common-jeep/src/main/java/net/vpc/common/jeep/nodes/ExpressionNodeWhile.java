/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionNode;
import net.vpc.common.jeep.ExpressionEvaluator;
import net.vpc.common.jeep.JeepUtils;

/**
 *
 * @author vpc
 */
public class ExpressionNodeWhile extends StatementNode {

    private ExpressionNode condition;
    private ExpressionNode block;

    public ExpressionNodeWhile() {
        super(null);
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public void setCondition(ExpressionNode condition) {
        this.condition = condition;
    }

    public ExpressionNode getBlock() {
        return block;
    }

    public void setBlock(ExpressionNode block) {
        this.block = block;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        return block == null ? Void.TYPE : block.getExprType(evaluator);
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        Object o = null;
        while (true) {
            boolean ok = false;
            if (condition == null) {
                ok = true;
            } else {
                ok = JeepUtils.convertToBoolean(condition.evaluate(evaluator));
            }
            evaluator.getExpressionManager().debug("##EXEC WHILE condition("+condition+") is "+ok);
            if (!ok) {
                break;
            }
            if (block != null) {
                o = block.evaluate(evaluator);
            }
        }
        return o;
    }

    @Override
    public String toString(String prefix) {
        StringBuilder sb=new StringBuilder();
        sb.append(prefix).append("while ").append(condition).append(NEWLINE);
        sb.append(block.toString(prefix + TAB)).append(NEWLINE);
        sb.append(prefix).append("end while");
        return sb.toString();
    }

}
