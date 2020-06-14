package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JShouldNeverHappenException;

public class JExpressionOptions implements Cloneable{
    public JExpressionUnaryOptions unary;
    public JExpressionBinaryOptions binary;

    public JExpressionOptions() {
    }

    public JExpressionOptions(JExpressionUnaryOptions unary, JExpressionBinaryOptions binary) {
        this.unary = unary;
        this.binary = binary;
    }

    public JExpressionOptions copy(){
        try {
            JExpressionOptions a=(JExpressionOptions) clone();
            if(a.unary!=null){
                a.unary=unary.copy();
            }
            if(a.binary!=null){
                a.binary=binary.copy();
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new JShouldNeverHappenException();
        }
    }
}
