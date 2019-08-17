/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionEvaluator;

/**
 *
 * @author vpc
 */
public class ExpressionNodeConst extends ExpressionNodeVariable {

    private final Object value;

    public ExpressionNodeConst(String name, Object value) {
        this(name,value,value==null?Object.class:value.getClass());
    }
    
    public ExpressionNodeConst(String name, Object value, Class type) {
        super(name, type == null ? value.getClass() : type);
        this.value = value;
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        return value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString(String prefix) {
        return prefix+getName();//+"=" + getValue();
    }
    

    
}
