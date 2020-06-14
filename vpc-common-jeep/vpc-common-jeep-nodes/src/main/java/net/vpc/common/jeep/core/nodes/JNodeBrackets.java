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
public class JNodeBrackets extends JNodeStatement {

    private JNode[] items;

    public JNodeBrackets(JNode[] items) {
        super();
        this.items = items;
    }

    public JNode[] getItems() {
        return items;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_BRACKETS;
    }

//    @Override
//    public JType getType(JContext context) {
//        return items[0].getType(context).toArray();
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC [" + item + "]");
//        return context.functions().evaluate("[", item);
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("[");
        for (int i = 0; i < items.length; i++) {
            if(i>0){
                sb.append(",");
            }
            JNode item = items[i];
            sb.append(item.toString());
        }
        sb.append("]");
        return sb.toString();
    }

}
