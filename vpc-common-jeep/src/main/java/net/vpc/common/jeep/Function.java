/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 * @author vpc
 */
public interface Function {

    Class getEffectiveResultType(ExpressionEvaluator evaluator, ExpressionNode[] args);

    Class getResultType(ExpressionManager evaluator, ExpressionNode[] args);

    boolean isVarArgs();
    
    Class[] getArgTypes();

    Object eval(ExpressionNode[] args, ExpressionEvaluator evaluator);

    String getName();

    MethodSignature getSignature() ;

}
