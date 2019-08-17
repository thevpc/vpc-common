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
 * @author vpc
 */
public class ExpressionNodeFor extends StatementNode {

    private String name;
    private ExpressionNode from;
    private ExpressionNode to;
    private ExpressionNode by;
    private ExpressionNode block;

    public ExpressionNodeFor() {
        super(null);
    }

    public ExpressionNode getBlock() {
        return block;
    }

    public void setBlock(ExpressionNode block) {
        this.block = block;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpressionNode getFrom() {
        return from;
    }

    public void setFrom(ExpressionNode from) {
        this.from = from;
    }

    public ExpressionNode getTo() {
        return to;
    }

    public void setTo(ExpressionNode to) {
        this.to = to;
    }

    public ExpressionNode getBy() {
        return by;
    }

    public void setBy(ExpressionNode by) {
        this.by = by;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        return Void.class;
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        evaluator.getExpressionManager().debug("##EXEC FOR");
        evaluator.setVariableValue(name, from.evaluate(evaluator));
        Object o = null;
        while (true) {
            ExpressionNodeVariableName index = evaluator.getExpressionManager().getVariableName(name);
            boolean ok = JeepUtils.convertToBoolean(evaluator.evaluateFunction("<=", index, to));
            evaluator.getExpressionManager().debug("##EXEC FOR condition=" + ok);
            if (!ok) {
                break;
            }
            if (block != null) {
                block.evaluate(evaluator);
            }
            ExpressionNode by2 = by;
            if (by2 == null) {
                by2 = new ExpressionNodeLiteral(JeepUtils.getIncDefaultValue(o));
            }
            evaluator.getExpressionManager().debug("##EXEC FOR, INC " + index);
            evaluator.setVariableValue(name, evaluator.evaluateFunction("+", index, by2));
        }
        return o;
    }

    public String toString(String prefix) {
        StringBuilder sb = new StringBuilder().append(prefix).append("for ").append(name).append("=").append(from);
        sb.append(" to ").append(to);
        if (by != null) {
            sb.append(" by ").append(by);
        }
        sb.append("\n");
        sb.append(block.toString(prefix + TAB)).append(NEWLINE);
        sb.append(prefix).append("end");
        return sb.toString();
    }
}
