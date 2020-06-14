/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core;

import net.vpc.common.jeep.*;
//import net.vpc.common.jeep.core.nodes.JNodeVarName;
//import net.vpc.common.jeep.core.nodes.JNodeVariable;
//import net.vpc.common.jeep.core.nodes.JNodeConst;
import net.vpc.common.jeep.impl.functions.JIdentFunction;
import net.vpc.common.jeep.impl.vars.JFieldVar;
import net.vpc.common.jeep.util.JeepUtils;

/**
 *
 * @author vpc
 */
public class JeepUtilsFactory {


    public static boolean createBoolean(Object u) {
        return JeepUtils.convertToBoolean(u);
    }

    public static DefaultJVar createVar(String name, Object value, JContext context) {
        return new DefaultJVar(name,
                context.types().typeOf(value),
                context.types().typeOf(value),
                value);
    }

    public static DefaultJVar createNullVar(String name, JType type) {
        return new DefaultJVar(name, type, type, null);
    }

    public static JVar createStaticFieldVar(JType type, String fieldName) {
        JField field = type.declaredFieldOrNull(fieldName);
        return field==null?null:createStaticFieldVar(field);
    }

    public static JVar createStaticFieldVar(JField field) {
        return new JFieldVar(null, field, true);
    }

    public static JVar createInstanceFieldVar(Object instance, String fieldName, JTypes types) {
        try {
            JType cls = types.typeOf(instance);
            JField found = null;
            while (cls != null) {
                found = cls.declaredFieldOrNull(fieldName);
                if (found != null) {
                    break;
                }
                cls = cls.getSuperType();
            }
            if (found == null) {
                throw new IllegalArgumentException("Field " + fieldName + "not found in " + instance.getClass());
            }
            return createInstanceFieldVar(instance, found);
        } catch (SecurityException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static JVar createInstanceFieldVar(Object instance, JField field) {
        return new JFieldVar(instance, field, true);
    }

    public static JFunction createIdentFunction(String name, JType type) {
        return new JIdentFunction(name, type);
    }

//    public static JNodeVariable createConstVarNode(String name, Object value, JType type) {
//        return new JNodeConst(name, value, type);
//    }
//    public static JNodeVarName createNameNode(String name, JType type) {
//        return new JNodeVarName(name);
//    }
}
