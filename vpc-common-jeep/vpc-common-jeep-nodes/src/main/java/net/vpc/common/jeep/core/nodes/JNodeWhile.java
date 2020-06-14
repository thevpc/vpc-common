/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.util.JeepUtils;

/**
 * @author vpc
 */
public class JNodeWhile extends JNodeStatement {

    private JNode condition;
    private JNode block;

    public JNodeWhile() {
        super();
    }


    @Override
    public int id() {
        return JNodeDefaultIds.NODE_WHILE;
    }

    public JNode getCondition() {
        return condition;
    }

    public void setCondition(JNode condition) {
        this.condition = condition;
    }

    public JNode getBlock() {
        return block;
    }

    public void setBlock(JNode block) {
        this.block = block;
    }

//    @Override
//    public JType getType(JContext context) {
//        return block == null ? context.types().forName(Void.TYPE) : block.getType(context);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        Object o = null;
//        while (true) {
//            boolean ok = false;
//            if (condition == null) {
//                ok = true;
//            } else {
//                ok = JeepUtils.convertToBoolean(condition.evaluate(context));
//            }
//            context.debug("##EXEC WHILE condition(" + condition + ") is " + ok);
//            if (!ok) {
//                break;
//            }
//            if (block != null) {
//                o = block.evaluate(context);
//            }
//        }
//        return o;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("while ").append(condition).append(JeepUtils.NEWLINE);
        sb.append(block.toString()).append(JeepUtils.NEWLINE);
        sb.append("end while");
        return sb.toString();
    }

}
