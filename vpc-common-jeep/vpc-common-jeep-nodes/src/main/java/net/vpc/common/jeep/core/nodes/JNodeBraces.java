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
public class JNodeBraces extends JNodeStatement {

    private JNode[] items;

    public JNodeBraces(JNode[] items) {
        super();
        this.items = items;
    }

    public JNode[] getItems() {
        return items;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_BRACES;
    }
//    @Override
//    public JType getType(JContext context) {
//        return item.getType(context);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC {" + item + "}");
//        return context.functions().evaluate("{", item);
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("{\n");
        for (int i = 0; i < items.length; i++) {
            if(i>0){
                sb.append(",");
            }
            JNode item = items[i];
            sb.append(item);
        }
        sb.append("\t}");
        return sb.toString();
    }

}
