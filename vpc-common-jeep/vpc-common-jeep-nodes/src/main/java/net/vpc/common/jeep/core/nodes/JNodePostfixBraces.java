/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;

/**
 * @author vpc
 */
public class JNodePostfixBraces extends JNodeStatement {

    private JNode base;
    private JNode items;

    public JNodePostfixBraces(JNode base, JNode items) {
        super();
        this.base = base;
        this.items = items;
    }

    public JNode getBase() {
        return base;
    }

    public void setBase(JNode base) {
        this.base = base;
    }

    public void setItems(JNodeBrackets items) {
        this.items = items;
    }

    public JNode getItems() {
        return items;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_BRACES_POSTFIX;
    }

//    @Override
//    public JType getType(JContext context) {
//        throw new IllegalArgumentException("Unsupported");
//        //return items[0].getType(context).toArray();
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        context.debug("##EXEC [" + item + "]");
//        return context.functions().evaluate("[", item);
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(base==null){
            sb.append("?");
        }else{
            sb.append(base.toString());
        }
        sb.append("{");
        sb.append(this.items);
        sb.append("}");
        return sb.toString();
    }

}
