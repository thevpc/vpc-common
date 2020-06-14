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
public class JNodePars extends JNodeStatement {

    private JNode[] items;

    public JNodePars(JNode[] items) {
        super();
        this.items = items;
    }

    public JNode[] getItems() {
        return items;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_PARS;
    }

//    @Override
//    public JType getType(JContext context) {
//        return JeepPlatformUtils.forObject(context.types());
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        Object o = null;
//        context.debug("##EXEC (" + item + ")");
//        o = item.evaluate(context);
//        return o;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("(");
        for (int i = 0; i < items.length; i++) {
            JNode item = items[i];
            if(i>0) {
                sb.append(",");
            }
            sb.append(item.toString());
        }
        sb.append(")");
        return sb.toString();
    }

}
