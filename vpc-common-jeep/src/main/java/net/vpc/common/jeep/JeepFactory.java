/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.lang.reflect.Field;
import net.vpc.common.jeep.nodes.ExpressionNodeConst;
import net.vpc.common.jeep.nodes.ExpressionNodeVariable;
import net.vpc.common.jeep.nodes.ExpressionNodeVariableName;

/**
 *
 * @author vpc
 */
public class JeepFactory {


    public static boolean createBoolean(Object u) {
        return JeepUtils.convertToBoolean(u);
    }

    public static DefaultVariable createVar(String name, Object value) {
        return new DefaultVariable(name, value);
    }

    public static DefaultVariable createNullVar(String name, Class type) {
        return new DefaultVariable(name, type, type, null);
    }

    public static Variable createStaticFieldVar(Class type, String fieldName) {
        try {
            return createStaticFieldVar(type.getDeclaredField(fieldName));
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException(ex);
        } catch (SecurityException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static Variable createStaticFieldVar(Field field) {
        return new FieldVariable(null, field, true);
    }

    public static Variable createInstanceFieldVar(Object instance, String fieldName) {
        try {
            Class cls = instance.getClass();
            Field found = null;
            while (cls != null) {
                try {
                    found = cls.getDeclaredField(fieldName);
                } catch (Exception any) {
                    //
                }
                if (found != null) {
                    break;
                }
                cls = cls.getSuperclass();
            }
            if (found == null) {
                throw new IllegalArgumentException("Field " + fieldName + "not found in " + instance.getClass());
            }
            return createInstanceFieldVar(instance, found);
        } catch (SecurityException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static Variable createInstanceFieldVar(Object instance, Field field) {
        return new FieldVariable(instance, field, true);
    }

    public static Function createConstFunction(String name, Object value) {
        return new ConstFunction(name, value);
    }

    public static ExpressionNodeVariable createConstVarNode(String name, Object value) {
        return new ExpressionNodeConst(name, value);
    }

    public static ExpressionNodeVariable createConstVarNode(String name, Object value, Class type) {
        return new ExpressionNodeConst(name, value, type);
    }
    public static ExpressionNodeVariableName createNameNode(String name, Class type) {
        return new ExpressionNodeVariableName(name, type);
    }
}
