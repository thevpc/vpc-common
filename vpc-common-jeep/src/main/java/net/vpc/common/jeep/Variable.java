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
public interface Variable {

    Class getUndefinedType();

    boolean isReadOnly();

    void setReadOnly(boolean readOnly);

    String getName();

    Class getEffectiveType(ExpressionEvaluator evaluator);
    
    Class getType();

    Object getValue(ExpressionEvaluator evaluator);

    Variable setValue(Object value, ExpressionEvaluator evaluator);

    boolean isDefinedValue();

    boolean isUndefinedValue();

    Variable setUndefinedValue();

}
