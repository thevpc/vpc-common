/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 *
 * @author vpc
 */
public abstract class AbstractVariable implements Variable {

    @Override
    public Class getEffectiveType(ExpressionEvaluator evaluator) {
        Object v = getValue(evaluator);
        if (v != null) {
            return v.getClass();
        }
        Class t = getType();
        if (t == null) {
            t = Object.class;
        }
        return t;
    }

}
