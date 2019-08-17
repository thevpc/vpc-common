/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionEvaluator;

/**
 * @author vpc
 */
public class ExpressionNodeBreak extends StatementNode {

    private final String name;

    public ExpressionNodeBreak(String name) {
        super(null);
        this.name = name;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        return Void.class;
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        throw new BreakException(name);
    }

    public String toString(String prefix) {
        return prefix + "break" + (name == null ? "" : (" " + name));
    }

}
