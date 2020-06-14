/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;

/**
 * @author vpc
 */
public class JNodeDotOperatorApply extends JNodeStatement {

    private final String name;
    private final JNode parent;

    public JNodeDotOperatorApply(String name, JNode parent) {
        super();
        this.name = name;
        this.parent = parent;
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

    public JNode getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return parent.toString() +
                "." + name;
    }


}
