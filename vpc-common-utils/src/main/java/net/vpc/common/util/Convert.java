/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.util;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class Convert {

    public static int toInteger(Object o) {
        return toInteger(o, null);
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

    public static double toDouble(Object o, DoubleParserConfig config) {
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
        if (o instanceof String) {
            try {
                return Double.parseDouble(((String) o));
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

    public static float toFloat(Object o, FloatParserConfig config) {
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
        if (o instanceof String) {
            try {
                return Float.parseFloat(((String) o));
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

    public static boolean toBoolean(Object o, BooleanParserConfig config) {
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
            return ((Boolean) o).booleanValue();
        }
        if (o instanceof Number) {
            if (config.isAcceptNumber()) {
                return ((Number) o).doubleValue() != 0;
            }
            if (config.isAcceptInt()) {
                if (PlatformTypes.isAnyIntegerValue(o)) {
                    return ((Number) o).longValue() != 0;
                }
            }
            return config.getInvalidValue();
        }
        if (o instanceof Character || o instanceof String) {
            String s = String.valueOf(o);
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

    public static int toInteger(Object o, IntegerParserConfig config) {
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
        if (o instanceof String || o instanceof Character) {
            String s = String.valueOf(o);
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

    public static long toLong(Object o, LongParserConfig config) {
        if (config == null) {
            config = LongParserConfig.STRICT;
        }
        if (o == null) {
            if (config.isErrorIfNull()) {
                throw new IllegalArgumentException("Cannot convert null to Integer");
            }
            return config.getNullValue();
        }
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        if (o instanceof String || o instanceof Character) {
            String s = String.valueOf(o);
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

}
