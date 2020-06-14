/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.vars;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JContext;
import net.vpc.common.jeep.JVar;

/**
 *
 * @author vpc
 */
public abstract class AbstractJVar implements JVar {

//    @Override
//    public JType getEffectiveType(JContext context) {
//        Object v = getValue(context);
//        if (v != null) {
//            return context.types().typeOf(v);
//        }
//        JType t = type();
//        if (t == null) {
//            t = context.types().forName(Object.class);
//        }
//        return t;
//    }

}
