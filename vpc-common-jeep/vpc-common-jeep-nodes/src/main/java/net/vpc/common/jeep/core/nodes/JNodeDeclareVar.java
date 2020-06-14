/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;

/**
 *
 * @author vpc
 */
public class JNodeDeclareVar extends JNodeStatement {

    private final JNode name;
    private final JNode value;
//    private final JType type;

    public JNodeDeclareVar(JNode name, JNode val) {
        super();
        this.name = name;
        this.value = val;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_DECLARE_VAR;
    }

    public JNode getName() {
        return name;
    }

    public JNode getValue() {
        return value;
    }

//    public JType getType() {
//        return type;
//    }

//    @Override
//    public JType getType(JContext context) {
//        return value.getType(context);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        Object result = value.evaluate(context);
//        context.vars().declareVar(
//                ((JNodeVariable)name).getName(),
//                context.types().forName(((JNodeVariable)type).getName()),
//                result);
//        return result;
//    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        if(getType()!=null){
            sb.append(getType().name()).append(" ");
        }else{
            sb.append("var").append(" ");
        }
        sb.append(name);
        sb.append("=").append(value);
        return sb.toString();
    }


}
