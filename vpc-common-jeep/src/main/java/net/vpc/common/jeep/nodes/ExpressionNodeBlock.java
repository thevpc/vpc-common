/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionNode;
import net.vpc.common.jeep.ExpressionEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class ExpressionNodeBlock extends StatementNode {

    private List<ExpressionNode> statements = new ArrayList<ExpressionNode>();

    public ExpressionNodeBlock() {
        super(null);
    }

    
    public void add(ExpressionNode node) {
        statements.add(node);
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        if (statements.size() > 0) {
            return statements.get(statements.size() - 1).getExprType(evaluator);
        }
        return Void.TYPE;
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        Object o = null;
        for (ExpressionNode statement : statements) {
            evaluator.getExpressionManager().debug("##EXEC "+statement);
            o = statement.evaluate(evaluator);
        }
        return o;
    }

    @Override
    public String toString(String prefix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statements.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(statements.get(i).toString(prefix));
        }
        return sb.toString();
    }

}
