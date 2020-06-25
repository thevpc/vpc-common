package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JShouldNeverHappenException;

public class JExpressionOptions implements Cloneable{
    private JExpressionUnaryOptions unary;
    private JExpressionBinaryOptions binary;

    public JExpressionOptions() {
    }

    public JExpressionOptions(JExpressionUnaryOptions unary, JExpressionBinaryOptions binary) {
        this.setUnary(unary);
        this.setBinary(binary);
    }

    public JExpressionOptions copy(){
        try {
            JExpressionOptions a=(JExpressionOptions) clone();
            if(a.getUnary() !=null){
                a.setUnary(getUnary().copy());
            }
            if(a.getBinary() !=null){
                a.setBinary(getBinary().copy());
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new JShouldNeverHappenException();
        }
    }

    public JExpressionUnaryOptions getUnary() {
        return unary;
    }

    public JExpressionOptions setUnary(JExpressionUnaryOptions unary) {
        this.unary = unary;
        return this;
    }

    public JExpressionBinaryOptions getBinary() {
        return binary;
    }

    public JExpressionOptions setBinary(JExpressionBinaryOptions binary) {
        this.binary = binary;
        return this;
    }
}
