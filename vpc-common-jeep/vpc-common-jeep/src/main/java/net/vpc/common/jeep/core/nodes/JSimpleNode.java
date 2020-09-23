package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.JNodeCopyFactory;
import net.vpc.common.jeep.JType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSimpleNode extends AbstractJNode{
    private String typeName;
    private Object[] arguments;

    public JSimpleNode(String typeName,Object... arguments) {
        this.typeName=typeName;
        this.arguments=arguments;
    }

    public String getTypeName() {
        return typeName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public List<JNode> getChildrenNodes() {
        List<JNode> li=new ArrayList<>();
        for (Object argument : arguments) {
            if(argument instanceof JNode){
                li.add((JNode) argument);
            }
        }
        return li;
    }

    @Override
    public void copyFrom(JNode other,JNodeCopyFactory copyFactory) {
        super.copyFrom(other);
        if (other instanceof JSimpleNode) {
            JSimpleNode s = (JSimpleNode) other;
            Object[] o_arguments = s.getArguments();
            Object[] arguments2= Arrays.copyOf(o_arguments,o_arguments.length);
            for (int i = 0; i < arguments2.length; i++) {
                if(arguments2[i] instanceof JNode){
                    arguments2[i]=((JNode)arguments2[i]).copy();
                }
            }
            this.typeName=s.typeName;
            this.arguments=arguments2;
        }
    }
}
