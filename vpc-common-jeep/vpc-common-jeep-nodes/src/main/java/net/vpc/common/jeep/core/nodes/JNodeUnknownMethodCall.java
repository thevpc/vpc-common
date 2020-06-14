/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.util.JNodeUtils;
import net.vpc.common.jeep.util.JeepUtils;

/**
 * @author vpc
 */
public class JNodeUnknownMethodCall extends JNodeStatement {

    private String name;
    private JNode parent;
    private JNode[] args;

    public JNodeUnknownMethodCall(JNode parent,String name,JNode[] args) {
        super();
        this.name = name;
        this.parent = parent;
        this.args = args;
    }

    public JNodeUnknownMethodCall setName(String name) {
        this.name = name;
        return this;
    }

    public JNodeUnknownMethodCall setParent(JNode parent) {
        this.parent = parent;
        return this;
    }

    public JNodeUnknownMethodCall setArgs(JNode[] args) {
        this.args = args;
        return this;
    }

    public JNode[] getArgs() {
        return args;
    }

    public JNode getParent() {
        return parent;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_SYNTACTIC;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Object.class);
//    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String n = getName();
        if (JeepUtils.isDefaultOp(n)) {
            switch (args.length) {
                case 1: {
                    return /*"(" + */ getName() + JNodeUtils.toPar(args[0])/*+ ")"*/;
                }
                case 2: {
                    return /*"(" +*/ JNodeUtils.toPar(args[0]) + getName() + JNodeUtils.toPar(args[1]) /*+ ")"*/;
                }
            }
        }
        StringBuilder sb = new StringBuilder().append(n).append("(");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String sargi = args[i].toString();
            sb.append(sargi);
        }
        sb.append(")");
        return sb.toString();
    }


}
