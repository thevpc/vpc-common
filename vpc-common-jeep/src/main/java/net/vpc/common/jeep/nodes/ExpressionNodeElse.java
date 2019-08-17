/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionEvaluator;

/**
 *
 * @author vpc
 */
public class ExpressionNodeElse extends StatementNode {

    public static final ExpressionNodeElse INSTANCE = new ExpressionNodeElse();

    public ExpressionNodeElse() {
        super(null);
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        return Void.class;
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        return null;
    }

    public String toString(String prefix) {
        return prefix + "else";
    }

}
