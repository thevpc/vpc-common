package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JShouldNeverHappenException;

import java.util.HashSet;
import java.util.Set;

public class JExpressionBinaryOptions implements Cloneable{
    public Set<String> excludedBinaryOperators;
    public boolean excludedListOperator;
    public boolean excludedImplicitOperator;

    public JExpressionBinaryOptions copy(){
        try {
            JExpressionBinaryOptions a=(JExpressionBinaryOptions) clone();
            if(a.excludedBinaryOperators!=null){
                a.excludedBinaryOperators=new HashSet<>(a.excludedBinaryOperators);
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new JShouldNeverHappenException();
        }
    }
}
