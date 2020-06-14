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
public class JNodePrefixUnaryOpCall extends JNodeStatement {

    private String name;
    private JNode arg;
    private JInvokablePrefilled impl;

    public JNodePrefixUnaryOpCall(String name,JNode arg) {
        super();
        this.name = name;
        this.arg = arg;
    }

    public String getName() {
        return name;
    }

    public JInvokablePrefilled impl() {
        return impl;
    }

    public JNodePrefixUnaryOpCall setImpl(JInvokablePrefilled impl) {
        this.impl = impl;
        return this;
    }

    public JNodePrefixUnaryOpCall setArg(JNode arg) {
        this.arg = arg;
        return this;
    }

    public JNode getArg() {
        return arg;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_OP_UNARY_PREFIX;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Object.class);
//    }


    @Override
    public String toString() {
        return name + JNodeUtils.toPar(arg);
    }


}