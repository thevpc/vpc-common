/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.JToken;

/**
 *
 * @author vpc
 */
public class JNodeAssign extends JNodeStatement {

    private JNode name;
    private JNode value;

    public JNodeAssign(JNode name, JNode val) {
        super();
        this.name = name;
        this.value = val;
    }

    public JNodeAssign setName(JNode name) {
        this.name = name;
        return this;
    }

    public JNodeAssign setValue(JNode value) {
        this.value = value;
        return this;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_ASSIGN;
    }

    public JNode getName() {
        return name;
    }

    public JNode getValue() {
        return value;
    }

//    @Override
//    public JType getType(JContext context) {
//        return value.getType(context);
//    }


//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC ASSIGN "+name);
//        Object v = context.evaluate(value);
//        context.vars().setValue(name, v);
//        return v;
//    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

}
