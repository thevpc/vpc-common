/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import net.vpc.common.jeep.nodes.ExpressionNodeLiteral;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
public class JeepUtils {
    public static final String OPERATORS = "-+$=<>#~%~/*|&!^";
    public static final int PRECEDENCE_0 = 1;
    public static final int PRECEDENCE_1 = 5;
    public static final int PRECEDENCE_2 = 10;
    public static final int PRECEDENCE_3 = 15;
    public static final int PRECEDENCE_4 = 20;
    public static final int PRECEDENCE_5 = 25;
    public static final int PRECEDENCE_6 = 30;
    public static final int PRECEDENCE_7 = 40;
    public static final int PRECEDENCE_MAX = 42;
    private static final ExpressionEvaluatorConverter[] ARR0 = new ExpressionEvaluatorConverter[0];
    private static Map<Class, ExpressionEvaluatorConverter[]> cache_getTypeImplicitConversions = new HashMap<>();

    public static boolean isDefaultOp(String c) {
        return (c.length() > 0 && OPERATORS.indexOf(c.charAt(0)) >= 0);
    }

    public static Object joinArraysAsType(Class componentType, Object[] arrays) {
        if (arrays.length == 0) {
            throw new IllegalArgumentException("No Array to join");
        }
        int lenSum = 0;
        for (int i = 0; i < arrays.length; i++) {
            Class<?> c = arrays[i].getClass().getComponentType();
            if (!componentType.isAssignableFrom(c)) {
                throw new IllegalArgumentException("Array expected");
            }
            lenSum += Array.getLength(arrays[i]);
        }
        int pos = 0;
        Object newObj = Array.newInstance(componentType, lenSum);
        for (int i = 0; i < arrays.length; i++) {
            int len = Array.getLength(arrays[i]);
            System.arraycopy(arrays[i], 0, newObj, pos, len);
            pos += len;
        }
        return newObj;
    }

    public static Object getIncDefaultValue(Object u) {
        if (u == null) {
            return 1;
        }
        if (u instanceof ExpressionNodeLiteral) {
            u = ((ExpressionNodeLiteral) u).getValue();
        }
        if (u == null) {
            return null;
        }
        if (u instanceof Number) {
            return 1;
        }
        throw new IllegalArgumentException("Invalid inc default value for " + u.getClass());
    }

    public static boolean convertToBoolean(Object u) {
        if (u == null) {
            return false;
        }
        if (u instanceof ExpressionNodeLiteral) {
            u = ((ExpressionNodeLiteral) u).getValue();
        }
        if (u == null) {
            return false;
        }
        if (u instanceof Boolean) {
            return ((Boolean) u).booleanValue();
        }
        if (u instanceof Number) {
            double d = ((Number) u).doubleValue();
            return Double.isNaN(d) && d != 0;
        }
        String s = (String) u.toString();
        if ("true".equalsIgnoreCase(s)) {
            return true;
        }
        try {
            double d = Double.parseDouble(s);
            return Double.isNaN(d) && d != 0;
        } catch (Exception ex) {
            //
        }
        return false;
    }

    public static long getDefaultBinaryOpPrecedence(String name) {
        if (name.isEmpty()) {
            return getDefaultBinaryOpPrecedence(' ');
        }
        long p = 0;
        for (char c : name.toCharArray()) {
            p = p * PRECEDENCE_MAX + getDefaultBinaryOpPrecedence(c);
        }
        return p;
    }

    public static int getDefaultBinaryOpPrecedence(char c) {
        switch (c) {
            case ',':
            case ';':
                return PRECEDENCE_1;
            case ' ':
                return PRECEDENCE_2;
            case '=':
            case '<':
            case '>':
            case '#':
            case '!':
                return PRECEDENCE_2;
            case '&':
            case '|':
            case '~':
                return PRECEDENCE_4;
            case '+':
            case '-':
                return PRECEDENCE_5;
            case '*':
            case '/':
                return PRECEDENCE_6;
            case '^':
            case '@':
            case '$':
            case ':':
                return PRECEDENCE_7;
        }
        return PRECEDENCE_6;
    }

    public static int getArrayDim(Class arrayType) {
        if (arrayType.isArray()) {
            return 1 + getArrayDim(arrayType.getComponentType());
        }
        return 0;
    }

    public static Class toArrType(Class arrayType) {
        return Array.newInstance(arrayType, 0).getClass();
    }

    public static Class toArrType(Class arrayType, int count) {
        Class t = arrayType;
        for (int i = 0; i < count; i++) {
            t = Array.newInstance(t, 0).getClass();

        }
        return t;
    }

    public static Class getArrayRootComponentType(Class arrayType) {
        if (arrayType.isArray()) {
            return getArrayRootComponentType(arrayType.getComponentType());
        }
        return arrayType;
    }

    public static String getSimpleClassName(Class[] cls) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cls.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getSimpleClassName(cls[i]));
        }
        return sb.toString();
    }

    public static String getSimpleClassName(Class cls) {
        if (cls.isArray()) {
            return getSimpleClassName(cls.getComponentType()) + "[]";
        }
        return cls.getSimpleName();
    }

    public static ExpressionEvaluatorConverter createTypeImplicitConversions(Class from, Class to) {
        String k = from.getName() + "/" + to.getName();
        ExpressionEvaluatorConverter f = JeepPlatformUtils.cached_createTypeImplicitConversions.get(k);
        if (f != null) {
            return f;
        }
        Class from1 = from;
        Class to1 = to;
        if (from1.isPrimitive()) {
            from1 = JeepPlatformUtils.toBoxingType(from1);
        }
        if (to1.isPrimitive()) {
            to1 = JeepPlatformUtils.toBoxingType(to1);
        }
        if (to1.isAssignableFrom(from1)) {
            return new CastExpressionEvaluatorConverter(from, to);
        }
        if (Number.class.isAssignableFrom(from1)) {
            if (Byte.class.equals(to1)) {
                f = new NumberToByteExpressionEvaluatorConverter(from, to);
            }
            if (Short.class.equals(to1)) {
                f = new NumberToShortExpressionEvaluatorConverter(from, to);
            }
            if (Integer.class.equals(to)) {
                f = new NumberToIntExpressionEvaluatorConverter(from, to);
            }
            if (Long.class.equals(to1)) {
                f = new NumberToLongExpressionEvaluatorConverter(from, to);
            }
            if (Float.class.equals(to1)) {
                f = new NumberToFloatExpressionEvaluatorConverter(from, to);
            }
            if (Double.class.equals(to1)) {
                f = new NumberToDoubleExpressionEvaluatorConverter(from, to);
            }
        }

        if (f == null) {
            throw new IllegalArgumentException("Unsupported implicit conversion from " + from.getName() + " to " + to.getName());
        }
        JeepPlatformUtils.cached_createTypeImplicitConversions.put(k, f);
        return f;
    }


    public static ExpressionEvaluatorConverter[] getTypeImplicitConversions(Class cls) {
        ExpressionEvaluatorConverter[] old = cache_getTypeImplicitConversions.get(cls);
        if (old != null) {
            return old;
        }
        if (cls.equals(Object.class) || cls.isPrimitive()) {
            cache_getTypeImplicitConversions.put(cls, ARR0);
            return ARR0;
        }
        List<ExpressionEvaluatorConverter> all = new ArrayList<ExpressionEvaluatorConverter>();
        if (cls.isArray()) {
            Class rct = getArrayRootComponentType(cls);
            if (!rct.isPrimitive() && !Object.class.equals(rct)) {
                all.add(new CastExpressionEvaluatorConverter(cls, toArrType(Object.class, getArrayDim(cls))));
            }
        }
        if (cls.getSuperclass() != null) {
            all.add(new CastExpressionEvaluatorConverter(cls, cls.getSuperclass()));
        }
        if (Boolean.TYPE.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Boolean.class));
        } else if (Boolean.class.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Boolean.TYPE));

        } else if (Byte.TYPE.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Byte.class));
            all.add(createTypeImplicitConversions(cls, Short.TYPE));
        } else if (Byte.class.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Byte.TYPE));

        } else if (Short.TYPE.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Short.class));
            all.add(createTypeImplicitConversions(cls, Integer.TYPE));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (Short.class.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Short.TYPE));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (Integer.TYPE.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Integer.class));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (Integer.class.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Integer.TYPE));
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));

        } else if (Float.TYPE.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Float.class));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (Float.class.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Float.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));

        } else if (Long.TYPE.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Long.class));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        } else if (Long.class.equals(cls)) {
            all.add(createTypeImplicitConversions(cls, Long.TYPE));
            all.add(createTypeImplicitConversions(cls, Double.TYPE));
        }
        for (Class cls2 : cls.getInterfaces()) {
            all.add(createTypeImplicitConversions(cls, cls2));
        }
        ExpressionEvaluatorConverter[] r = all.toArray(new ExpressionEvaluatorConverter[all.size()]);
        cache_getTypeImplicitConversions.put(cls, r);
        return r;
    }

    public static void validateOperator(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Empty operator");
        }
        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                throw new IllegalArgumentException("operator could not contain white characters : " + name);
            }
        }
    }

    private static class NumberToByteExpressionEvaluatorConverter extends AbstractExpressionEvaluatorConverter {
        public NumberToByteExpressionEvaluatorConverter(Class from, Class to) {
            super(from, to);
        }

        @Override
        public Object convert(Object value) {
            if (value == null) {
                return null;
            }
            return ((Number) value).byteValue();
        }
    }

    private static class NumberToShortExpressionEvaluatorConverter extends AbstractExpressionEvaluatorConverter {
        public NumberToShortExpressionEvaluatorConverter(Class from, Class to) {
            super(from, to);
        }

        @Override
        public Object convert(Object value) {
            if (value == null) {
                return null;
            }
            return ((Number) value).shortValue();
        }
    }

    private static class NumberToIntExpressionEvaluatorConverter extends AbstractExpressionEvaluatorConverter {
        public NumberToIntExpressionEvaluatorConverter(Class from, Class to) {
            super(from, to);
        }

        @Override
        public Object convert(Object value) {
            if (value == null) {
                return null;
            }
            return ((Number) value).intValue();
        }
    }

    private static class NumberToLongExpressionEvaluatorConverter extends AbstractExpressionEvaluatorConverter {
        public NumberToLongExpressionEvaluatorConverter(Class from, Class to) {
            super(from, to);
        }

        @Override
        public Object convert(Object value) {
            if (value == null) {
                return null;
            }
            return ((Number) value).longValue();
        }
    }

    private static class NumberToFloatExpressionEvaluatorConverter extends AbstractExpressionEvaluatorConverter {
        public NumberToFloatExpressionEvaluatorConverter(Class from, Class to) {
            super(from, to);
        }

        @Override
        public Object convert(Object value) {
            if (value == null) {
                return null;
            }
            return ((Number) value).floatValue();
        }
    }

    private static class NumberToDoubleExpressionEvaluatorConverter extends AbstractExpressionEvaluatorConverter {
        public NumberToDoubleExpressionEvaluatorConverter(Class from, Class to) {
            super(from, to);
        }

        @Override
        public Object convert(Object value) {
            if (value == null) {
                return null;
            }
            return ((Number) value).doubleValue();
        }
    }
}
