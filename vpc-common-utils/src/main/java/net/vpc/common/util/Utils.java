/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author taha.bensalah@gmail.com
 */
public class Utils {
    public static final int TYPE_NULLABLE = 1;
    public static final int TYPE_NUMBER = 2;
    public static final int TYPE_BOOLEAN = 3;
    public static final int TYPE_STRING = 4;
    public static final int TYPE_TEMPORAL = 5;
    public static final int TYPE_OTHER = 6;
    public static final int TYPE_INT = (1 << 3) + TYPE_NUMBER;
    public static final int TYPE_FLOAT = (2 << 3) + TYPE_NUMBER;
    public static final int TYPE_DECIMAL = (3 << 3) + TYPE_NUMBER;
    public static final int TYPE_TIME = (1 << 3) + TYPE_NUMBER;
    public static final int TYPE_DATE = (2 << 3) + TYPE_NUMBER;
    public static final int TYPE_DATETIME = (3 << 3) + TYPE_NUMBER;
    public static final int TYPE_TIMESTAMP = (4 << 3) + TYPE_NUMBER;
    public static final int TYPE_MONTH = (5 << 3) + TYPE_NUMBER;
    public static final int TYPE_YEAR = (6 << 3) + TYPE_NUMBER;
    public static final int TYPE_8 = (1 << 5);
    public static final int TYPE_16 = (2 << 5);
    public static final int TYPE_32 = (3 << 5);
    public static final int TYPE_64 = (4 << 5);
    public static final int TYPE_128 = (5 << 5);
    public static final int TYPE_BIG = (6 << 5);
    private final static Map<Class, Object> DEFAULT_VALUES_BY_TYPE = new HashMap<Class, Object>();
    private final static Map<Class, Integer> TYPE_TO_INT_FLAGS = new HashMap<Class, Integer>(20);
    private final static Map<Class, Class> PRIMITIVE_TO_REF_TYPES = new HashMap<Class, Class>();
    private final static Map<Class, Class> REF_TO_PRIMITIVE_TYPES = new HashMap<Class, Class>();
    private static final Map<Class, String> typeNames = new HashMap<Class, String>();

    static {
        DEFAULT_VALUES_BY_TYPE.put(Short.TYPE, (short) 0);
        DEFAULT_VALUES_BY_TYPE.put(Long.TYPE, 0L);
        DEFAULT_VALUES_BY_TYPE.put(Integer.TYPE, 0);
        DEFAULT_VALUES_BY_TYPE.put(Double.TYPE, 0.0);
        DEFAULT_VALUES_BY_TYPE.put(Float.TYPE, 0.0f);
        DEFAULT_VALUES_BY_TYPE.put(Byte.TYPE, (byte) 0);
        DEFAULT_VALUES_BY_TYPE.put(Character.TYPE, (char) 0);

        PRIMITIVE_TO_REF_TYPES.put(Short.TYPE, Short.class);
        PRIMITIVE_TO_REF_TYPES.put(Long.TYPE, Long.class);
        PRIMITIVE_TO_REF_TYPES.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TO_REF_TYPES.put(Double.TYPE, Double.class);
        PRIMITIVE_TO_REF_TYPES.put(Float.TYPE, Float.class);
        PRIMITIVE_TO_REF_TYPES.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TO_REF_TYPES.put(Character.TYPE, Character.class);

        REF_TO_PRIMITIVE_TYPES.put(Short.class, Short.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Long.class, Long.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Integer.class, Integer.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Double.class, Double.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Float.class, Float.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Byte.class, Byte.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Character.class, Character.TYPE);

        typeNames.put(String.class, "string");
        typeNames.put(Integer.class, "int");
        typeNames.put(Integer.TYPE, "int");
        typeNames.put(Short.class, "short");
        typeNames.put(Short.TYPE, "short");
        typeNames.put(Byte.class, "byte");
        typeNames.put(Byte.TYPE, "byte");
        typeNames.put(Long.class, "long");
        typeNames.put(Long.TYPE, "long");
        typeNames.put(Float.class, "float");
        typeNames.put(Float.TYPE, "float");
        typeNames.put(Double.class, "double");
        typeNames.put(Double.TYPE, "double");
        typeNames.put(Boolean.class, "boolean");
        typeNames.put(Boolean.TYPE, "boolean");
        typeNames.put(java.util.Date.class, "datetime");
        typeNames.put(java.sql.Date.class, "date");
        typeNames.put(java.sql.Time.class, "time");
        typeNames.put(java.sql.Timestamp.class, "timestamp");
        typeNames.put(BigInteger.class, "bigint");
        typeNames.put(BigDecimal.class, "bigdecimal");
        typeNames.put(Object.class, "object");
        typeNames.put(byte[].class, "bytearray");
        typeNames.put(Byte[].class, "bytearray");
        typeNames.put(char[].class, "chararray");
        typeNames.put(Character[].class, "chararray");
    }

    public static int getPlatformType(Class cls) {
        Integer value = TYPE_TO_INT_FLAGS.get(cls);
        if (value == null) {
            if (Boolean.class.equals(cls)) {
                value = TYPE_BOOLEAN | TYPE_NULLABLE;
            } else if (Boolean.TYPE.equals(cls)) {
                value = TYPE_BOOLEAN | TYPE_NULLABLE;
            } else if (Byte.class.equals(cls)) {
                value = TYPE_INT | TYPE_8 | TYPE_NULLABLE;
            } else if (Byte.TYPE.equals(cls)) {
                value = TYPE_INT | TYPE_8;
            } else if (Short.class.equals(cls)) {
                value = TYPE_INT | TYPE_16 | TYPE_NULLABLE;
            } else if (Short.TYPE.equals(cls)) {
                value = TYPE_INT | TYPE_16;
            } else if (Integer.class.equals(cls)) {
                value = TYPE_INT | TYPE_32 | TYPE_NULLABLE;
            } else if (Integer.TYPE.equals(cls)) {
                value = TYPE_INT | TYPE_32;
            } else if (Long.class.equals(cls)) {
                value = TYPE_INT | TYPE_64 | TYPE_NULLABLE;
            } else if (Long.TYPE.equals(cls)) {
                value = TYPE_INT | TYPE_64;
            } else if (BigInteger.class.equals(cls)) {
                value = TYPE_INT | TYPE_BIG | TYPE_NULLABLE;
            } else if (Float.class.equals(cls)) {
                value = TYPE_FLOAT | TYPE_32 | TYPE_NULLABLE;
            } else if (Float.TYPE.equals(cls)) {
                value = TYPE_FLOAT | TYPE_32;
            } else if (Double.class.equals(cls)) {
                value = TYPE_FLOAT | TYPE_64 | TYPE_NULLABLE;
            } else if (Double.TYPE.equals(cls)) {
                value = TYPE_FLOAT | TYPE_64;
            } else if (BigDecimal.class.equals(cls)) {
                value = TYPE_DECIMAL | TYPE_BIG | TYPE_NULLABLE;
            } else if (String.class.equals(cls)) {
                value = TYPE_STRING | TYPE_NULLABLE;
            } else {
                value = TYPE_OTHER | TYPE_NULLABLE;
            }

            TYPE_TO_INT_FLAGS.put(cls, value);
        }
        return value.intValue();
    }

    public static String getSimpleTypeName(Class clazz) {
        return typeNames.get(clazz);
    }

    public static boolean isPrimitiveOrBoxed(Class type) {
        if (type.isPrimitive()) {
            return true;
        }
        Class r = REF_TO_PRIMITIVE_TYPES.get(type);
        if (r != null) {
            return true;
        }
        return false;
    }

    public static boolean isSimpleType(Class type) {
        if (type.equals(String.class)) {
            return true;
        }
        return isPrimitiveOrBoxed(type);
    }

    public static Class toRefType(Class type) {
        Class t = PRIMITIVE_TO_REF_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static Class toPrimitiveType(Class type) {
        Class t = REF_TO_PRIMITIVE_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static boolean isEmpty(Object object) {
        if (object instanceof CharSequence) {
            String s = ((CharSequence) object).toString().trim();
            if (s.length() > 0) {
                return false;
            }
            return true;
        }
        return object != null;
    }

    public static Object nonNullValue(Object... objects) {
        if (objects == null) {
            return null;
        }
        for (Object object : objects) {
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    public static Object nonEmptyValue(Object... objects) {
        if (objects == null) {
            return null;
        }
        for (Object object : objects) {
            if (!isEmpty(object)) {
                return object;
            }
        }
        return null;
    }

    public static int fibonacci(int number) {
        if (number == 1 || number == 2) {
            return 1;
        }
        int fibo1 = 1, fibo2 = 1, fibonacci = 1;
        for (int i = 3; i <= number; i++) {
            fibonacci = fibo1 + fibo2; //Fibonacci number is sum of previous two Fibonacci number
            fibo1 = fibo2;
            fibo2 = fibonacci;

        }
        return fibonacci; //Fibonacci number
    }

    public static int rand(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static <T> T rand(List<T> values) {
        return values.get(rand(0, values.size()));
    }

    public static <T> T rand(T[] values) {
        return values[rand(0, values.length)];
    }

    public static <T> T rand(Class<T> enumType) {
        T[] arr = (T[]) enumType.getEnumConstants();
        return rand(arr);
    }

    public static <K, V> MapList<K, V> unmodifiableMapList(MapList<K, V> list) {
        return list == null ? null : new UnmodifiableMapList<>(list);
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        return map == null ? null : Collections.unmodifiableMap(map);
    }

    public static <V> List<V> unmodifiableList(List<V> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    public static <V> Collection<V> unmodifiableCollection(Collection<V> list) {
        return list == null ? null : Collections.unmodifiableCollection(list);
    }

    public static <T> Collection<T> retainAll(Collection<T> values, Filter<T> filter) {
        if (filter == null) {
            throw new NullPointerException("Filter could not be null");
        }
        for (Iterator<T> i = values.iterator(); i.hasNext(); ) {
            if (!filter.accept(i.next())) {
                i.remove();
            }
        }
        return values;
    }

    public static <T> Collection<T> removeAll(Collection<T> values, Filter<T> filter) {
        if (filter == null) {
            throw new NullPointerException("Filter could not be null");
        }
        for (Iterator<T> i = values.iterator(); i.hasNext(); ) {
            if (filter.accept(i.next())) {
                i.remove();
            }
        }
        return values;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        try {
            Float.parseFloat(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isDate(String str, String format) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        try {
            new SimpleDateFormat(format).parse(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isShort(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        try {
            Short.parseShort(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isByte(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return false;
        }
        try {
            Byte.parseByte(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(double d) {
        if ((d == Math.floor(d)) && !Double.isInfinite(d)) {
            long x = (long) Math.floor(d);
            if (x == (int) x) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLong(double d) {
        if ((d == Math.floor(d)) && !Double.isInfinite(d)) {
            return true;
        }
        return false;
    }

    public static Boolean[] toBooleanArray(boolean[] arr) {
        Boolean[] r = new Boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static boolean[] toPrimitiveBooleanArray(Boolean[] arr) {
        boolean[] r = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static boolean[] toSafePrimitiveBooleanArray(Boolean[] arr) {
        boolean[] r = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Boolean v = arr[i];
            r[i] = v == null ? false : v;
        }
        return r;
    }

    public static boolean[] toPrimitiveBooleanArray(Collection<Boolean> arr) {
        boolean[] r = new boolean[arr.size()];
        int i = 0;
        for (Boolean v : arr) {
            r[i] = v;
            i++;
        }
        return r;
    }

    public static boolean[] toSafePrimitiveBooleanArray(Collection<Boolean> arr) {
        boolean[] r = new boolean[arr.size()];
        int i = 0;
        for (Boolean v : arr) {
            r[i] = v == null ? false : v;
            i++;
        }
        return r;
    }


    public static Integer[] toIntArray(int[] arr) {
        Integer[] r = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static int[] toPrimitiveIntArray(Integer[] arr) {
        int[] r = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static int[] toSafePrimitiveIntArray(Integer[] arr) {
        int[] r = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Integer v = arr[i];
            r[i] = v == null ? 0 : v;
        }
        return r;
    }

    public static int[] toPrimitiveIntArray(Collection<Integer> arr) {
        int[] r = new int[arr.size()];
        int i = 0;
        for (Integer v : arr) {
            r[i] = v;
            i++;
        }
        return r;
    }

    public static int[] toSafePrimitiveIntArray(Collection<Integer> arr) {
        int[] r = new int[arr.size()];
        int i = 0;
        for (Integer v : arr) {
            r[i] = v == null ? 0 : v;
            i++;
        }
        return r;
    }


    public static Long[] toLongArray(long[] arr) {
        Long[] r = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static long[] toPrimitiveLongArray(Long[] arr) {
        long[] r = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static long[] toSafePrimitiveLongArray(Long[] arr) {
        long[] r = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Long v = arr[i];
            r[i] = v == null ? 0 : v;
        }
        return r;
    }

    public static long[] toPrimitiveLongArray(Collection<Long> arr) {
        long[] r = new long[arr.size()];
        int i = 0;
        for (Long v : arr) {
            r[i] = v;
            i++;
        }
        return r;
    }

    public static long[] toSafePrimitiveLongArray(Collection<Long> arr) {
        long[] r = new long[arr.size()];
        int i = 0;
        for (Long v : arr) {
            r[i] = v == null ? 0 : v;
            i++;
        }
        return r;
    }

    public static Double[] toDoubleArray(double[] arr) {
        Double[] r = new Double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static double[] toPrimitiveDoubleArray(Double[] arr) {
        double[] r = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            r[i] = arr[i];
        }
        return r;
    }

    public static double[] toSafePrimitiveDoubleArray(Double[] arr) {
        double[] r = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            Double v = arr[i];
            r[i] = v == null ? 0 : v;
        }
        return r;
    }

    public static double[] toPrimitiveDoubleArray(Collection<Double> arr) {
        double[] r = new double[arr.size()];
        int i = 0;
        for (Double v : arr) {
            r[i] = v;
            i++;
        }
        return r;
    }

    public static double[] toSafePrimitiveDoubleArray(Collection<Double> arr) {
        double[] r = new double[arr.size()];
        int i = 0;
        for (Double v : arr) {
            r[i] = v == null ? 0 : v;
            i++;
        }
        return r;
    }

    public static long inUseMemory() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory());
    }

    public static long maxFreeMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());
    }

    /**
     * created a view on the List where each element is replaced by it converter
     *
     * @param from
     * @param converter
     * @param <F>
     * @param <T>
     * @return
     */
    public <F, T> List<T> convertList(final List<F> from, final Converter<F, T> converter) {
        if (converter == null) {
            throw new NullPointerException("Null converter");
        }
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                F value = from.get(index);
                return converter.convert(value);
            }

            @Override
            public T remove(int index) {
                F removed = from.remove(index);
                if (removed == null) {
                    return null;
                }
                return converter.convert(removed);
            }

            @Override
            public int size() {
                return from.size();
            }
        };
    }

}
