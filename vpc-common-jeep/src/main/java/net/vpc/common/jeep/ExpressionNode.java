/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 * @author vpc
 */
public interface ExpressionNode {

    String TAB = "\t";
    String NEWLINE = "\n";

    Class getEffectiveExprType(ExpressionEvaluator evaluator);

    Class getExprType(ExpressionManager evaluator);

    Object evaluate(ExpressionEvaluator evaluator);

    String toString(String prefix);
}
