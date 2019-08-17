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
public class ExpressionNodeLiteral extends AbstractExpressionNode {

    private final Object value;

    public ExpressionNodeLiteral(Object value) {
        this(value, value == null ? Object.class : value.getClass());
    }

    public ExpressionNodeLiteral(Object value, Class type) {
        super(type == null ? value.getClass() : type);
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
        if(value instanceof String){
            StringBuilder sb=new StringBuilder("\"");
            for (char c : value.toString().toCharArray()) {
                switch (c){
                    case '\"':{
                        sb.append("\\\"");
                        break;
                    }
                    case '\n':{
                        sb.append("\\n");
                        break;
                    }
                    default:{
                        sb.append(c);
                    }
                }
            }
            sb.append("\"");
            return prefix+String.valueOf(sb);
        }
        return prefix+String.valueOf(value);
    }

}
