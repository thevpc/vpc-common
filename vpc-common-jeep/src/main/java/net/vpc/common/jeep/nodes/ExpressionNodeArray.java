/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionNode;
import net.vpc.common.jeep.ExpressionEvaluator;
import net.vpc.common.jeep.JeepPlatformUtils;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 *
 * @author vpc
 */
public class ExpressionNodeArray extends AbstractExpressionNode {

    private final String arrayType;
    private final ExpressionNode[] values;

    public ExpressionNodeArray(String arrayType,ExpressionNode[] values) {
        this(arrayType,values, null);
    }

    public ExpressionNodeArray(String arrayType,ExpressionNode[] values, Class type) {
        super(type == null ? Object.class : type);
        this.values = values;
        this.arrayType= arrayType;
    }

    public String getArrayType() {
        return arrayType;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        Class o = null;
        for (ExpressionNode value : values) {
            if (value != null) {
                o = o == null ? value.getExprType(evaluator) : JeepPlatformUtils.firstCommonSuperType(o, value.getExprType(evaluator));
            }
        }
        return o == null ? Object[].class : Array.newInstance(o, 0).getClass();
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        Object[] arr = (Object[]) Array.newInstance(getEffectiveExprType(evaluator).getComponentType(), values.length);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values[i].evaluate(evaluator);
        }
        return arr;
    }

    public ExpressionNode get(int i) {
        return values[i];
    }
    
    public ExpressionNode[] getValues() {
        return values;
    }


    @Override
    public String toString(String prefix) {
        return (prefix==null?"":prefix)+Arrays.toString(values);
    }
}
