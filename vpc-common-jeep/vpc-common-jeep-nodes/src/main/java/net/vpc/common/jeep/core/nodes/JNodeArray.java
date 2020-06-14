/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.*;

import java.util.Arrays;

/**
 *
 * @author vpc
 */
public class JNodeArray extends JDefaultNode {

    private final String arrayType;
    private final JNode[] values;

    public JNodeArray(String arrayType, JNode[] values) {
        super();
        this.values = values;
        this.arrayType= arrayType;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_ARRAY;
    }

    public String getArrayType() {
        return arrayType;
    }

//    @Override
//    public JType getType(JContext context) {
//        JType o = null;
//        for (JNode value : values) {
//            if (value != null) {
//                o = o == null ? value.getType(context) : o.firstCommonSuperType(value.getType(context));
//            }
//        }
//        if(o==null){
//            return context.types().forName(Object.class).toArray(1);
//        }else{
//            return o.toArray(1);
//        }
//    }

    public JNode get(int i) {
        return values[i];
    }
    
    public JNode[] getValues() {
        return values;
    }


    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}