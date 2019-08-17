/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author vpc
 */
public class ExpressionNodeFunctionCall extends StatementNode {

    private final String name;
    private final ExpressionNode[] args;

    public ExpressionNodeFunctionCall(String name, ExpressionNode[] args) {
        super(Object.class);
        this.name = name;
        this.args = args;
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        return evaluator.evaluateFunction(getName(), getArgs());
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        Function f = evaluator.findFunction(getName(), getArgs());
        if(f==null){
            throw new NoSuchElementException("Function not found "+getName()+Arrays.asList(getArgs()));
//            return Object.class;
        }
        return f.getResultType(evaluator, args);
    }

    public String getName() {
        return name;
    }

    public ExpressionNode[] getArgs() {
        return Arrays.copyOf(args, args.length);
    }

    private String toPar(ExpressionNode e) {
        if (e instanceof ExpressionNodeLiteral
                || e instanceof ExpressionNodeArray
                || e instanceof ExpressionNodeConst
                || e instanceof ExpressionNodeVariable
                || e instanceof ExpressionNodeVariableName) {
            return e.toString("");
        }
        return "(" + e.toString("") + ")";
    }

    @Override
    public String toString(String prefix) {
        String n = getName();
        if (JeepUtils.isDefaultOp(n)) {
            switch (args.length) {
                case 1: {
                    return /*"(" + */ getName() + toPar(args[0])/*+ ")"*/;
                }
                case 2: {
                    return /*"(" +*/ toPar(args[0]) + getName() + toPar(args[1]) /*+ ")"*/;
                }
            }
        }
        StringBuilder sb = new StringBuilder(prefix == null ? "" : prefix).append(n).append("(");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String sargi = args[i].toString();
            sb.append(sargi);
        }
        sb.append(")");
        return sb.toString();
    }

    public ExpressionNode get(int index) {
        return args[index];
    }

    public ExpressionNode getOperand(int index) {
        return args[index];
    }

    public boolean isBinary(String name) {
        return getName().equals(name) && isBinary();
    }

    public boolean isUnary(String name) {
        return getName().equals(name) && isUnary();
    }

    public boolean is(String name) {
        return getName().equals(name);
    }

    public boolean isBinary() {
        return args.length == 2;
    }

    public boolean isUnary() {
        return args.length == 1;
    }

    public int getOperandsCount() {
        return args.length;
    }

    public ExpressionNode[] getOperands() {
        return Arrays.copyOf(args, args.length);
    }

}
