/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.jeep;

/**
 * @author vpc
 */
public class NoSuchVariableException extends ExpressionEvaluatorException {
    private String varName;

    public NoSuchVariableException(String varName) {
        super("Variable " + varName + " not found");
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }


}
