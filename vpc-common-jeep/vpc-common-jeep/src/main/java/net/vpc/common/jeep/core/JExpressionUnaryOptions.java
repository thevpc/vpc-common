package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JShouldNeverHappenException;

import java.util.HashSet;
import java.util.Set;

public class JExpressionUnaryOptions implements Cloneable{
    public Set<String> excludedPrefixUnaryOperators;
    public boolean excludedPrefixParenthesis;
    public boolean excludedPrefixBrackets;
    public boolean excludedPrefixBraces;

    public boolean excludedTerminalParenthesis;
    public boolean excludedTerminalBrackets;
    public boolean excludedTerminalBraces;

    public boolean excludedTerminal;
    public Set<String> excludedPostfixUnaryOperators;
    public boolean excludedPostfixParenthesis;
    public boolean excludedPostfixBrackets;
    public boolean excludedPostfixBraces;
    public JExpressionUnaryOptions copy(){
        try {
            JExpressionUnaryOptions a=(JExpressionUnaryOptions) clone();
            if(a.excludedPrefixUnaryOperators!=null){
                a.excludedPrefixUnaryOperators=new HashSet<>(a.excludedPrefixUnaryOperators);
            }
            if(a.excludedPostfixUnaryOperators!=null){
                a.excludedPostfixUnaryOperators=new HashSet<>(a.excludedPostfixUnaryOperators);
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new JShouldNeverHappenException();
        }
    }
}
