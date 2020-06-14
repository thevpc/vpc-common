/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;

import java.util.ArrayList;
import java.util.List;

import net.vpc.common.jeep.util.JeepUtils;

/**
 *
 * @author vpc
 */
public class JNodeBlock extends JNodeStatement {

    private List<JNode> statements = new ArrayList<JNode>();

    public JNodeBlock() {
        super();
    }

    
    public void add(JNode node) {
        statements.add(node);
    }
    @Override
    public int id() {
        return JNodeDefaultIds.NODE_BLOCK;
    }

//    @Override
//    public JType getType(JContext context) {
//        if (statements.size() > 0) {
//            return statements.get(statements.size() - 1).getType(context);
//        }
//        return context.types().forName(Void.TYPE);
//    }

//    @Override
//    public Object evaluate(JContext context) {
//        Object o = null;
//        for (JNode statement : statements) {
//            context.debug("##EXEC "+statement);
//            o = statement.evaluate(context);
//        }
//        return o;
//    }

    public List<JNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statements.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            JNode n = statements.get(i);
            String t = n.toString();
            sb.append(JeepUtils.indent(t));
        }
        return sb.toString();
    }

}
