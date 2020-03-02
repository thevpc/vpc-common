/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionEvaluator;
import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.Variable;

/**
 *
 * @author vpc
 */
public class ExpressionNodeSuffixedIntNumber extends ExpressionNodeVariable {
    private String number;
    private String suffix;
    public ExpressionNodeSuffixedIntNumber(String number,String suffix, Class type) {
        super(number+suffix, type);
        this.number=number;
        this.suffix=suffix;
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        return evaluator.getVariableValue(getName());
    }

    public String toString(String prefix) {
        return prefix + getName();
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        Variable o = evaluator.findVariable(getName());
        if(o==null){
            return null;
        }
        return o.getType();
//        return o==null?null:o.getEffectiveType(evaluator);
    }
}
