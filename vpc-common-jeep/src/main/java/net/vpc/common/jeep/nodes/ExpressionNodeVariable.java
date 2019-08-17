/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

/**
 * @author vpc
 */
public abstract class ExpressionNodeVariable extends AbstractExpressionNode {

    private final String name;

    public ExpressionNodeVariable(String name, Class returnType) {
        super(returnType);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(String prefix) {
        return prefix+getName();
    }
}
