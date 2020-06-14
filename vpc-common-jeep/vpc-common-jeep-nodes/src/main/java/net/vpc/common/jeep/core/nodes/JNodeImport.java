/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JNode;

/**
 *
 * @author vpc
 */
public class JNodeImport extends JNodeStatement {

    private final String name;
    private JType type;
    private final JNode value;

    public JNodeImport(String name, JNode val) {
        super();
        this.name = name;
        this.value = val;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_IMPORT;
    }

    public String getName() {
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
//        Object result = context.evaluate(value);
//        context.vars().declareVar(name,type, result);
//        return result;
//    }

    @Override
    public String toString() {
        return name + "=" + value;
    }


}