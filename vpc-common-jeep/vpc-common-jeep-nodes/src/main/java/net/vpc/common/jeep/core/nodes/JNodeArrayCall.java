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
public class JNodeArrayCall extends JDefaultNode {

    private final JNode base;
    private final JNodeArray arrayIndex;

    public JNodeArrayCall(JNode base, JNodeArray arrayIndex) {
        super();
        this.base = base;
        this.arrayIndex = arrayIndex;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_ARRAY_CALL;
    }

    public JNode getBase() {
        return base;
    }

    public JNodeArray getArrayIndex() {
        return arrayIndex;
    }

//    @Override
//    public JType getType(JContext context) {
//        return arrayIndex.getType(context);
//    }

    @Override
    public String toString() {
        return base.toString()+ arrayIndex;
    }

}
