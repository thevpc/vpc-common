/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JContext;
import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.util.JeepUtils;

/**
 *
 * @author vpc
 */
public class JNodeIf extends JNodeStatement {

    private JNode condition;
    private JNode trueBlock;
    private JNode falseBlock;

    public JNodeIf() {
        super();
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_IF;
    }

    public JNode getCondition() {
        return condition;
    }

    public void setCondition(JNode condition) {
        this.condition = condition;
    }

    public JNode getTrueBlock() {
        return trueBlock;
    }

    public void setTrueBlock(JNode trueBlock) {
        this.trueBlock = trueBlock;
    }

    public JNode getFalseBlock() {
        return falseBlock;
    }

    public void setFalseBlock(JNode falseBlock) {
        this.falseBlock = falseBlock;
    }

    public static boolean evalBoolean(JNode condition, JContext context) {
        boolean ok = false;
        if (condition == null) {
            ok = true;
        } else {
            Object u = context.evaluate(condition);
            if (u instanceof JNodeLiteral) {
                u = ((JNodeLiteral) u).getValue();
            }
            if (u instanceof Boolean) {
                ok = ((Boolean) u).booleanValue();
            }
        }
        return ok;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("if ");
        sb.append(condition).append(JeepUtils.NEWLINE);
        sb.append(trueBlock.toString()).append(JeepUtils.NEWLINE);
        if (falseBlock != null) {
            sb.append("else").append(JeepUtils.NEWLINE);
            sb.append(falseBlock.toString()).append(JeepUtils.NEWLINE);
        }
        sb.append("end");
        return sb.toString();
    }


}
