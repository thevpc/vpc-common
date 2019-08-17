/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionNode;
import net.vpc.common.jeep.ExpressionEvaluator;

/**
 * @author vpc
 */
public abstract class AbstractExpressionNode implements ExpressionNode {

    private Class resultType;

    public AbstractExpressionNode(Class resultType) {
        this.resultType = resultType;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        return resultType;
    }

    @Override
    public Class getEffectiveExprType(ExpressionEvaluator evaluator) {
        return resultType;
    }

    @Override
    public String toString() {
        return toString("");
    }

}
