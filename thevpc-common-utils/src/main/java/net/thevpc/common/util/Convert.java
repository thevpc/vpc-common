/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.util;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class Convert {

    public static int toInt(Object o) {
        return toInt(o, null);
    }

    public static long toLong(Object o) {
        return toLong(o, null);
    }

    public static double toDouble(Object o) {
        return toDouble(o, null);
    }

    public static float toFloat(Object o) {
        return toFloat(o, null);
    }

    public static boolean toBoolean(Object o) {
        return toBoolean(o, null);
    }

    public static Double toDouble(Object o, DoubleParserConfig config) {
        if (config == null) {
            config = DoubleParserConfig.STRICT;
        }
        if (o == null) {
            if (config.isErrorIfNull()) {
                throw new IllegalArgumentException("Cannot convert null to Double");
            }
            return config.getNullValue();
        }
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        if (o instanceof CharSequence || o instanceof Character) {
            String s = String.valueOf(o);
            s = s.trim();
            if (s.isEmpty()) {
                if (config.isErrorIfNull()) {
                    throw new IllegalArgumentException("Cannot convert null to Long");
                }
                return config.getNullValue();
            }
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                if (config.isErrorIfInvalid()) {
                    throw new IllegalArgumentException("Cannot convert string " + o + " to Double", e);
                }
                return config.getInvalidValue();
            }
        }
        if (config.isErrorIfInvalid()) {
            throw new IllegalArgumentException("Cannot convert " + o.getClass() + " to Double");
        }
        return config.getInvalidValue();
    }

    public static Float toFloat(Object o, FloatParserConfig config) {
        if (config == null) {
            config = FloatParserConfig.STRICT;
        }
        if (o == null) {
            if (config.isErrorIfNull()) {
                throw new IllegalArgumentException("Cannot convert null to Double");
            }
            return config.getNullValue();
        }
        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        if (o instanceof CharSequence || o instanceof Character) {
            String s = String.valueOf(o);
            s = s.trim();
            if (s.isEmpty()) {
                if (config.isErrorIfNull()) {
                    throw new IllegalArgumentException("Cannot convert null to Long");
                }
                return config.getNullValue();
            }
            try {
                return Float.parseFloat(s);
            } catch (Exception e) {
                if (config.isErrorIfInvalid()) {
                    throw new IllegalArgumentException("Cannot convert string " + o + " to Float", e);
                }
                return config.getInvalidValue();
            }
        }
        if (config.isErrorIfInvalid()) {
            throw new IllegalArgumentException("Cannot convert " + o.getClass() + " to Float");
        }
        return config.getInvalidValue();
    }

    public static Boolean toBoolean(Object o, BooleanParserConfig config) {
        if (config == null) {
            config = BooleanParserConfig.LENIENT;
        }
        if (o == null) {
            if (config.isErrorIfNull()) {
                throw new IllegalArgumentException("Cannot convert null to Boolean");
            }
            return config.getNullValue();
        }
        if (o instanceof Boolean) {
            return ((Boolean) o);//.booleanValue();
        }
        if (o instanceof Number) {
            if (config.isAcceptNumber()) {
                return ((Number) o).doubleValue() != 0;
            }
            if (config.isAcceptInt()) {
                if (PlatformUtils.isAnyIntegerValue(o)) {
                    return ((Number) o).longValue() != 0;
                }
            }
            return config.getInvalidValue();
        }
        if (o instanceof CharSequence || o instanceof Character) {
            String s = String.valueOf(o);
            s = s.trim();
            if (s.isEmpty()) {
                if (config.isErrorIfNull()) {
                    throw new IllegalArgumentException("Cannot convert null to Boolean");
                }
                return config.getNullValue();
            }
            Boolean isTrue = null;
            Boolean isFalse = null;
            if (config.getTrueStringRegexp() != null && config.getTrueStringRegexp().length() > 0) {
                isTrue = s.matches(config.getTrueStringRegexp());
            }
            if (config.getFalseStringRegexp() != null && config.getFalseStringRegexp().length() > 0) {
                isTrue = s.matches(config.getFalseStringRegexp());
            }
            if (isTrue == null && isFalse == null) {
                return "true".equalsIgnoreCase(s);
            } else if (isTrue != null && isFalse != null) {
                if (isTrue && isFalse) {
                    return config.getInvalidValue();
                } else {
                    return isTrue;
                }
            } else if (isTrue != null && isFalse == null) {
                return isTrue;
            } else if (isTrue == null && isFalse != null) {
                return !isFalse;
            }
        }
        if (config.isErrorIfInvalid()) {
            throw new IllegalArgumentException("Cannot convert " + o.getClass() + " to Double");
        }
        return config.getInvalidValue();
    }

    public static Integer toInt(Object o, IntegerParserConfig config) {
        if (config == null) {
            config = IntegerParserConfig.STRICT;
        }
        if (o == null) {
            if (config.isErrorIfNull()) {
                throw new IllegalArgumentException("Cannot convert null to Integer");
            }
            return config.getNullValue();
        }
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        if (o instanceof CharSequence || o instanceof Character) {
            String s = String.valueOf(o);
            s = s.trim();
            if (s.isEmpty()) {
                if (config.isErrorIfNull()) {
                    throw new IllegalArgumentException("Cannot convert null to Integer");
                }
                return config.getNullValue();
            }
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                if (config.isErrorIfInvalid()) {
                    throw new IllegalArgumentException("Cannot convert string " + o + " to Integer", e);
                }
                return config.getInvalidValue();
            }
        }
        if (config.isErrorIfInvalid()) {
            throw new IllegalArgumentException("Cannot convert " + o.getClass() + " to Integer");
        }
        return config.getInvalidValue();
    }

    public static Long toLong(Object o, LongParserConfig config) {
        if (config == null) {
            config = LongParserConfig.STRICT;
        }
        if (o == null) {
            if (config.isErrorIfNull()) {
                throw new IllegalArgumentException("Cannot convert null to Long");
            }
            return config.getNullValue();
        }
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        if (o instanceof CharSequence || o instanceof Character) {
            String s = String.valueOf(o);
            s = s.trim();
            if (s.isEmpty()) {
                if (config.isErrorIfNull()) {
                    throw new IllegalArgumentException("Cannot convert null to Long");
                }
                return config.getNullValue();
            }
            try {
                return Long.parseLong(s);
            } catch (Exception e) {
                if (config.isErrorIfInvalid()) {
                    throw new IllegalArgumentException("Cannot convert string " + o + " to Long", e);
                }
                return config.getInvalidValue();
            }
        }
        if (config.isErrorIfInvalid()) {
            throw new IllegalArgumentException("Cannot convert " + o.getClass() + " to Long");
        }
        return config.getInvalidValue();
    }

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public static String toNonNullString(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    public static Number toNumber(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {

        }
        try {
            return Long.parseLong(s);
        } catch (Exception e) {

        }
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {

        }
        return null;
    }

    public static Integer[] toIntArray(String[] strings, IntegerParserConfig config) {
        if (config == null) {
            config = IntegerParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        Integer[] values = new Integer[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toInt(strings[i], config);
        }
        return values;
    }

    public static int[] toPrimitiveIntArray(String[] strings, IntegerParserConfig config) {
        if (config == null) {
            config = IntegerParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        int[] values = new int[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toInt(strings[i], config);
        }
        return values;
    }

    public static Long[] toLongArray(String[] strings, LongParserConfig config) {
        if (config == null) {
            config = LongParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        Long[] values = new Long[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toLong(strings[i], config);
        }
        return values;
    }

    public static long[] toPrimitiveLongArray(String[] strings, LongParserConfig config) {
        if (config == null) {
            config = LongParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        long[] values = new long[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toLong(strings[i], config);
        }
        return values;
    }

    public static Double[] toDoubleArray(String[] strings, DoubleParserConfig config) {
        if (config == null) {
            config = DoubleParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        Double[] values = new Double[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toDouble(strings[i], config);
        }
        return values;
    }

    public static double[] toPrimitiveLongArray(String[] strings, DoubleParserConfig config) {
        if (config == null) {
            config = DoubleParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        double[] values = new double[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toDouble(strings[i], config);
        }
        return values;
    }

    public static Boolean[] toBooleanArray(String[] strings, BooleanParserConfig config) {
        if (config == null) {
            config = BooleanParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        Boolean[] values = new Boolean[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toBoolean(strings[i], config);
        }
        return values;
    }

    public static boolean[] toPrimitiveBooleanArray(String[] strings, BooleanParserConfig config) {
        if (config == null) {
            config = BooleanParserConfig.STRICT;
        }
        if (strings == null) {
            return null;
        }
        boolean[] values = new boolean[strings.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = toBoolean(strings[i], config);
        }
        return values;
    }
}
