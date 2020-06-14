/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JInvokablePrefilled;
import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.util.JNodeUtils;

/**
 * @author vpc
 */
public class JNodeInfixBinaryOperatorCall extends JNodeStatement {

    private final String name;
    private JNode arg1;
    private JNode arg2;
    private JInvokablePrefilled impl;

    public JNodeInfixBinaryOperatorCall(String name, JNode arg1, JNode arg2) {
        super();
        this.name = name;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public JInvokablePrefilled getImpl() {
        return impl;
    }

    public JNodeInfixBinaryOperatorCall setImpl(JInvokablePrefilled impl) {
        this.impl = impl;
        return this;
    }

    public JNodeInfixBinaryOperatorCall setArg1(JNode arg1) {
        this.arg1 = arg1;
        return this;
    }

    public JNodeInfixBinaryOperatorCall setArg2(JNode arg2) {
        this.arg2 = arg2;
        return this;
    }

    public JNode getArg1() {
        return arg1;
    }

    public JNode getArg2() {
        return arg2;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_OP_BINARY_INFIX;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.functions().findMatchOrNull(getName(),arg1,arg2).returnType();
//    }

    @Override
    public String toString() {
        return JNodeUtils.toPar(arg1) + getName() + JNodeUtils.toPar(arg2);
    }

    public String getName() {
        return name;
    }


}
