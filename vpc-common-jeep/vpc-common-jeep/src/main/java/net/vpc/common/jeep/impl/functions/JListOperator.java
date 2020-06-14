/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JFunctionBase;
import net.vpc.common.jeep.JTypeArray;

/**
 *
 * @author vpc
 */
public class JListOperator extends JFunctionBase {
    
    private JInvoke operator;
    private JTypeArray operandTypeArr;

    public JListOperator(JInvoke operator, String name, JType resultType, JType operandType) {
        //            super(name, resultType, true, toArrayClass(operandType));
        super(name, resultType, new JType[]{operandType.toArray(1)}, true);
        this.operator = operator;
        this.operandTypeArr = (JTypeArray) operandType.toArray(1);
    } //            super(name, resultType, true, toArrayClass(operandType));

    @Override
    public Object invoke(JInvokeContext icontext) {
        return operator.invoke(icontext);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String n = name();
        if (n == null) {
            n = "<IMPLICIT>";
        }
        sb.append(n).append("(");
        sb.append(operandTypeArr.componentType().simpleName()).append("...");
        sb.append(")");
        return "ExpressionListOperator{" + "operator=" + operator + ", resultType=" + returnType() + ", operandTypeArr=" + operandTypeArr + '}';
    }
    
}
