/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionNode;
import net.vpc.common.jeep.ExpressionEvaluator;

/**
 *
 * @author vpc
 */
public class ExpressionNodeAssign extends StatementNode {

    private final String name;
    private final ExpressionNode value;

    public ExpressionNodeAssign(String name, ExpressionNode val) {
        super(null);
        this.name = name;
        this.value = val;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getValue() {
        return value;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        return value.getExprType(evaluator);
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        evaluator.getExpressionManager().debug("##EXEC ASSIGN "+name);
        Object v = value.evaluate(evaluator);
        evaluator.setVariableValue(name, v);
        return v;
    }

    @Override
    public String toString(String prefix) {
        return prefix + name + "=" + value;
    }

}
