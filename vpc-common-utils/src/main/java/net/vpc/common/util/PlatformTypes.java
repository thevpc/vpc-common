/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class PlatformTypes {

    public static boolean isIntegerType(Class o) {
        if (o == null) {
            return false;
        }
        if (Integer.class.isAssignableFrom(o)
                || Integer.TYPE.isAssignableFrom(o)) {
            return true;
        }
        return false;
    }

    public static boolean isLongType(Class o) {
        if (o == null) {
            return false;
        }
        if (Long.class.isAssignableFrom(o)
                || Long.TYPE.isAssignableFrom(o)) {
            return true;
        }
        return false;
    }

    public static boolean isAnyIntegerType(Class o) {
        if (o == null) {
            return false;
        }
        if (Byte.class.isAssignableFrom(o)
                || Integer.class.isAssignableFrom(o)
                || Long.class.isAssignableFrom(o)
                || BigInteger.class.isAssignableFrom(o)
                || Byte.TYPE.isAssignableFrom(o)
                || Integer.TYPE.isAssignableFrom(o)
                || Long.TYPE.isAssignableFrom(o)) {
            return true;
        }
        return false;
    }

    public static boolean isDateType(Class o) {
        if (o == null) {
            return false;
        }
        return java.util.Date.class.isAssignableFrom(o);
    }

    public static boolean isAnyFloatType(Class o) {
        if (o == null) {
            return false;
        }
        if (Float.class.isAssignableFrom(o)
                || Double.class.isAssignableFrom(o)
                || BigDecimal.class.isAssignableFrom(o)
                || Float.TYPE.isAssignableFrom(o)
                || Double.TYPE.isAssignableFrom(o)) {
            return true;
        }
        return false;
    }

    public static boolean isFloatType(Class o) {
        if (o == null) {
            return false;
        }
        if (Float.class.isAssignableFrom(o)
                || Float.TYPE.isAssignableFrom(o)) {
            return true;
        }
        return false;
    }

    public static boolean isDoubleType(Class o) {
        if (o == null) {
            return false;
        }
        if (Double.class.isAssignableFrom(o)
                || Double.TYPE.isAssignableFrom(o)) {
            return true;
        }
        return false;
    }

    public static boolean isBooleanType(Class o) {
        if (o == null) {
            return false;
        }
        if (Boolean.class.isAssignableFrom(o)
                || Boolean.TYPE.isAssignableFrom(o)) {
            return true;
        }
        return false;
    }

    public static boolean isAnyIntegerValue(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Byte || o instanceof Integer || o instanceof Long || o instanceof BigInteger) {
            return true;
        }
        return false;
    }

    public static boolean isAnyFloatValue(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Float || o instanceof Double || o instanceof BigDecimal) {
            return true;
        }
        return false;
    }

    public static boolean isDateValue(Object o) {
        if (o == null) {
            return false;
        }
        return java.util.Date.class.isInstance(o);
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {

        }
        return false;
    }
    
    public static boolean isAnyInteger(String s) {
        try {
            new BigInteger(s);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

}
