package net.thevpc.common.util;

import java.lang.reflect.AccessibleObject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlatformUtils {
    public static final int TYPE_NULLABLE = 1;
    public static final int TYPE_NUMBER = 2;
    public static final int TYPE_YEAR = (6 << 3) + TYPE_NUMBER;
    public static final int TYPE_MONTH = (5 << 3) + TYPE_NUMBER;
    public static final int TYPE_TIMESTAMP = (4 << 3) + TYPE_NUMBER;
    public static final int TYPE_DATETIME = (3 << 3) + TYPE_NUMBER;
    public static final int TYPE_DATE = (2 << 3) + TYPE_NUMBER;
    public static final int TYPE_TIME = (1 << 3) + TYPE_NUMBER;
    public static final int TYPE_DECIMAL = (3 << 3) + TYPE_NUMBER;
    public static final int TYPE_FLOAT = (2 << 3) + TYPE_NUMBER;
    public static final int TYPE_INT = (1 << 3) + TYPE_NUMBER;
    public static final int TYPE_BOOLEAN = 3;
    public static final int TYPE_STRING = 4;
    public static final int TYPE_TEMPORAL = 5;
    public static final int TYPE_OTHER = 6;
    public static final int TYPE_8 = (1 << 5);
    public static final int TYPE_16 = (2 << 5);
    public static final int TYPE_32 = (3 << 5);
    public static final int TYPE_64 = (4 << 5);
    public static final int TYPE_128 = (5 << 5);
    public static final int TYPE_BIG = (6 << 5);
    private static final Map<Class, Object> DEFAULT_VALUES_BY_TYPE = new HashMap<Class, Object>();
    private static final Map<Class, Integer> TYPE_TO_INT_FLAGS = new HashMap<Class, Integer>(20);
    private static final Map<Class, Class> PRIMITIVE_TO_REF_TYPES = new HashMap<Class, Class>();
    private static final Map<Class, Class> REF_TO_PRIMITIVE_TYPES = new HashMap<Class, Class>();
    private static final Map<Class, String> typeNames = new HashMap<Class, String>();

    static {
        DEFAULT_VALUES_BY_TYPE.put(Short.TYPE, (short) 0);
        DEFAULT_VALUES_BY_TYPE.put(Long.TYPE, 0L);
        DEFAULT_VALUES_BY_TYPE.put(Integer.TYPE, 0);
        DEFAULT_VALUES_BY_TYPE.put(Double.TYPE, 0.0);
        DEFAULT_VALUES_BY_TYPE.put(Float.TYPE, 0.0f);
        DEFAULT_VALUES_BY_TYPE.put(Byte.TYPE, (byte) 0);
        DEFAULT_VALUES_BY_TYPE.put(Character.TYPE, (char) 0);
        DEFAULT_VALUES_BY_TYPE.put(Boolean.TYPE, Boolean.FALSE);

        PRIMITIVE_TO_REF_TYPES.put(Short.TYPE, Short.class);
        PRIMITIVE_TO_REF_TYPES.put(Long.TYPE, Long.class);
        PRIMITIVE_TO_REF_TYPES.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TO_REF_TYPES.put(Double.TYPE, Double.class);
        PRIMITIVE_TO_REF_TYPES.put(Float.TYPE, Float.class);
        PRIMITIVE_TO_REF_TYPES.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TO_REF_TYPES.put(Character.TYPE, Character.class);
        PRIMITIVE_TO_REF_TYPES.put(Boolean.TYPE, Boolean.class);

        REF_TO_PRIMITIVE_TYPES.put(Short.class, Short.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Long.class, Long.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Integer.class, Integer.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Double.class, Double.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Float.class, Float.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Byte.class, Byte.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Character.class, Character.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Boolean.class, Boolean.TYPE);
    }

    private static Comparator<Class> CLASS_HIERARCHY_COMPARATOR = new Comparator<Class>() {
        @Override
        public int compare(Class o1, Class o2) {
            if (o1.isAssignableFrom(o2)) {
                return 1;
            } else if (o2.isAssignableFrom(o1)) {
                return -1;
            }
            if (o1.isInterface() && !o2.isInterface()) {
                return 1;
            }
            if (o2.isInterface() && !o1.isInterface()) {
                return -1;
            }
            return 0;
        }
    };

    private static Comparator<TypeReference> TYPE_REFERENCE_HIERARCHY_COMPARATOR = new Comparator<TypeReference>() {
        @Override
        public int compare(TypeReference o1, TypeReference o2) {
            if (o1.isAssignableFrom(o2)) {
                return 1;
            } else if (o2.isAssignableFrom(o1)) {
                return -1;
            }
            if (o1.isInterface() && !o2.isInterface()) {
                return 1;
            }
            if (o2.isInterface() && !o1.isInterface()) {
                return -1;
            }
            return 0;
        }
    };
    private static Comparator<TypeName> TYPE_REFERENCE_STR_HIERARCHY_COMPARATOR = new Comparator<TypeName>() {
        @Override
        public int compare(TypeName o1, TypeName o2) {
            if (o1.isAssignableFrom(o2)) {
                return 1;
            } else if (o2.isAssignableFrom(o1)) {
                return -1;
            }
            if (o1.isInterface() && !o2.isInterface()) {
                return 1;
            }
            if (o2.isInterface() && !o1.isInterface()) {
                return -1;
            }
            return 0;
        }
    };
    public static Class lowestCommonAncestor(Class a, Class b) {
        if (a.equals(b)) {
            return a;
        }
        if (a.isAssignableFrom(b)) {
            return a;
        }
        if (b.isAssignableFrom(a)) {
            return b;
        }
        Class[] aHierarchy = findClassHierarchy(a, null);
        Class[] bHierarchy = findClassHierarchy(b, null);
        int i1 = -1;
        int i2 = -1;
        for (int ii = 0; ii < aHierarchy.length; ii++) {
            for (int jj = 0; jj < bHierarchy.length; jj++) {
                if (aHierarchy[ii].equals(bHierarchy[jj])) {
                    if (i1 < 0 || ii + jj < i1 + i2) {
                        i1 = ii;
                        i2 = jj;
                    }
                }
            }
        }
        if (i1 < 0) {
            return Object.class;
        }
        return aHierarchy[i1];
    }
    public static Class[] findClassHierarchy(Class clazz, Class baseType) {
        HashSet<Class> seen = new HashSet<Class>();
        Queue<Class> queue = new LinkedList<Class>();
        List<Class> result = new LinkedList<Class>();
        queue.add(clazz);
        while (!queue.isEmpty()) {
            Class i = queue.remove();
            if (baseType == null || baseType.isAssignableFrom(i)) {
                if (!seen.contains(i)) {
                    seen.add(i);
                    result.add(i);
                    if (i.getSuperclass() != null) {
                        queue.add(i.getSuperclass());
                    }
                    for (Class ii : i.getInterfaces()) {
                        queue.add(ii);
                    }
                }
            }
        }
        Collections.sort(result, CLASS_HIERARCHY_COMPARATOR);
        return result.toArray(new Class[result.size()]);
    }

    public static TypeReference[] findClassHierarchy(TypeReference clazz, TypeReference baseType) {
        HashSet<TypeReference> seen = new HashSet<TypeReference>();
        Queue<TypeReference> queue = new LinkedList<TypeReference>();
        List<TypeReference> result = new LinkedList<TypeReference>();
        queue.add(clazz);
        while (!queue.isEmpty()) {
            TypeReference i = queue.remove();
            if (baseType == null || baseType.isAssignableFrom(i)) {
                if (!seen.contains(i)) {
                    seen.add(i);
                    result.add(i);
                    if (i.getSuperclass() != null) {
                        queue.add(i.getSuperclass());
                    }
                    for (TypeReference ii : i.getInterfaces()) {
                        queue.add(ii);
                    }
                }
            }
        }
        Collections.sort(result, TYPE_REFERENCE_HIERARCHY_COMPARATOR);
        return result.toArray(new TypeReference[result.size()]);
    }

    public static TypeName[] findClassHierarchy(TypeName clazz, TypeName baseType) {
        HashSet<TypeName> seen = new HashSet<TypeName>();
        Queue<TypeName> queue = new LinkedList<TypeName>();
        List<TypeName> result = new LinkedList<TypeName>();
        queue.add(clazz);
        while (!queue.isEmpty()) {
            TypeName i = queue.remove();
            if (baseType == null || baseType.isAssignableFrom(i)) {
                if (!seen.contains(i)) {
                    seen.add(i);
                    result.add(i);
                    if (i.getSuperclass() != null) {
                        queue.add(i.getSuperclass());
                    }
                    for (TypeName ii : i.getInterfaces()) {
                        queue.add(ii);
                    }
                }
            }
        }
        Collections.sort(result, TYPE_REFERENCE_STR_HIERARCHY_COMPARATOR);
        return result.toArray(new TypeName[0]);
    }

    public static Class[] findClassOnlyHierarchy(Class clazz, Class baseType) {
        HashSet<Class> seen = new HashSet<Class>();
        Queue<Class> queue = new LinkedList<Class>();
        List<Class> result = new LinkedList<Class>();
        queue.add(clazz);
        while (!queue.isEmpty()) {
            Class i = queue.remove();
            if (baseType == null || baseType.isAssignableFrom(i)) {
                if (!seen.contains(i)) {
                    seen.add(i);
                    result.add(i);
                    if (i.getSuperclass() != null) {
                        queue.add(i.getSuperclass());
                    }
                }
            }
        }
        Collections.sort(result, CLASS_HIERARCHY_COMPARATOR);
        return result.toArray(new Class[result.size()]);
    }


    public static TypeReference lowestCommonAncestor(TypeReference a, TypeReference b) {
        if (a.equals(b)) {
            return a;
        }
        if (a.isAssignableFrom(b)) {
            return a;
        }
        if (b.isAssignableFrom(a)) {
            return b;
        }
        TypeReference[] aHierarchy = findClassHierarchy(a, null);
        TypeReference[] bHierarchy = findClassHierarchy(b, null);
        int i1 = -1;
        int i2 = -1;
        for (int ii = 0; ii < aHierarchy.length; ii++) {
            for (int jj = 0; jj < bHierarchy.length; jj++) {
                if (aHierarchy[ii].equals(bHierarchy[jj])) {
                    if (i1 < 0 || ii + jj < i1 + i2) {
                        i1 = ii;
                        i2 = jj;
                    }
                }
            }
        }
        if (i1 < 0) {
            return TypeReference.of(Object.class);
        }
        return aHierarchy[i1];
    }
    public static TypeName lowestCommonAncestor(TypeName a, TypeName b) {
        if (a.equals(b)) {
            return a;
        }
        if (a.isAssignableFrom(b)) {
            return a;
        }
        if (b.isAssignableFrom(a)) {
            return b;
        }
        TypeName[] aHierarchy = findClassHierarchy(a, null);
        TypeName[] bHierarchy = findClassHierarchy(b, null);
        int i1 = -1;
        int i2 = -1;
        for (int ii = 0; ii < aHierarchy.length; ii++) {
            for (int jj = 0; jj < bHierarchy.length; jj++) {
                if (aHierarchy[ii].equals(bHierarchy[jj])) {
                    if (i1 < 0 || ii + jj < i1 + i2) {
                        i1 = ii;
                        i2 = jj;
                    }
                }
            }
        }
        if (i1 < 0) {
            return new TypeName(Object.class.getName());
        }
        return aHierarchy[i1];
    }

    public static List<Class> commonAncestors(Class a, Class b) {
        Class[] aHierarchy = findClassHierarchy(a, null);
        Class[] bHierarchy = findClassHierarchy(b, null);
        int i1 = -1;
        int i2 = -1;
        List<Class> all = new ArrayList<>();
        for (int ii = 0; ii < aHierarchy.length; ii++) {
            for (int jj = 0; jj < bHierarchy.length; jj++) {
                if (aHierarchy[ii].equals(bHierarchy[jj])) {
                    all.add(aHierarchy[ii]);
                }
            }
        }
        return all;
    }
    public static Class[] getPrimitiveBoxingTypes() {
        return REF_TO_PRIMITIVE_TYPES.keySet().toArray(new Class[REF_TO_PRIMITIVE_TYPES.size()]);
    }

    public static Class[] getPrimitiveTypes() {
        return PRIMITIVE_TO_REF_TYPES.keySet().toArray(new Class[PRIMITIVE_TO_REF_TYPES.size()]);
    }

    public static Object getDefaultValue(Class type) {
        return DEFAULT_VALUES_BY_TYPE.get(type);
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

    public static boolean isPlatformNumber(Class n) {
        return n.getName().startsWith("java.lang");
    }

    public static boolean isPlatformNumber(Number n) {
        return n.getClass().getName().startsWith("java.lang");
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
        return Date.class.isAssignableFrom(o);
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
        return Date.class.isInstance(o);
    }

    public static boolean isAnyInteger(String s) {
        try {
            new BigInteger(s);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (str.length() == 0) {
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
        if (str.length() == 0) {
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
        if (str.length() == 0) {
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
        if (str.length() == 0) {
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
        if (str.length() == 0) {
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
        if (str.length() == 0) {
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
        if (str.length() == 0) {
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

//    public static Method getMatchingMethod(Class cls, String methodName, Class... parameterTypes) {
//        return getMatchingMethod(true, cls, methodName, parameterTypes);
//    }

//    public static Method getMatchingMethod(boolean accessibleOnly, Class cls, String methodName, Class... parameterTypes) {
//        try {
//            Method method = cls.getMethod(methodName, parameterTypes);
//            setAccessibleWorkaround(method);
//            return method;
//        } catch (NoSuchMethodException e) { // NOPMD - Swallow the exception
//        }
//        // search through all methods
//        Method[] methods = accessibleOnly ? cls.getMethods() : cls.getDeclaredMethods();//cls.getMethods();
//        List<MethodObject<Method>> m = new ArrayList<>();
//        for (int i = 0; i < methods.length; i++) {
//            if(methods[i].getName().equals(methodName)) {
//                m.add(new MethodObject<>(
//                        new MethodSignature(
//                                methods[i].getParameterTypes(),
//                                methods[i].isVarArgs()
//                        ), methods[i]
//                ));
//            }
//        }
//        MethodObject<Method> rr = getMatchingMethod(m.toArray(new MethodObject[0]), parameterTypes);
//        if (rr == null) {
//            return null;
//        }
//        setAccessibleWorkaround(rr.method);
//        return rr.method;
//    }

    public static Class toBoxingType(Class type) {
        Class t = PRIMITIVE_TO_REF_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static Class toPrimitiveTypeOrNull(Class type) {
        if (type.isPrimitive()) {
            return type;
        }
        Class t = REF_TO_PRIMITIVE_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return null;
    }

    public static Class toPrimitiveType(Class type) {
        Class t = REF_TO_PRIMITIVE_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static boolean isAssignableFrom(Class parent, Class child) {
        if (parent.isAssignableFrom(child)) {
            return true;
        }
        if (toBoxingType(parent).isAssignableFrom(toBoxingType(child))) {
            return true;
        }
        Class c1 = toPrimitiveTypeOrNull(parent);
        Class c2 = toPrimitiveTypeOrNull(child);
        if (c1 != null && c2 != null) {
            if (Boolean.TYPE.equals(c1)) {
                return c2.equals(Boolean.TYPE);
            }
            if (Byte.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE);
            }
            if (Short.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE);
            }
            if (Character.TYPE.equals(c1)) {
                return c2.equals(Character.TYPE);
            }
            if (Integer.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE);
            }
            if (Long.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE);
            }
            if (Float.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE);
            }
            if (Double.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE) || c2.equals(Float.TYPE) || c2.equals(Double.TYPE);
            }
        }
        return false;
    }

    public static int getAssignationCost(Class parent, Class child) {
        if (parent.equals(child)) {
            return 0;
        }
        if (parent.isAssignableFrom(child)) {
            return 1;
        }
        if (toBoxingType(parent).isAssignableFrom(toBoxingType(child))) {
            return 1;
        }
        Class c1 = toPrimitiveTypeOrNull(parent);
        Class c2 = toPrimitiveTypeOrNull(child);
        if (c1 != null && c2 != null) {
            if (c1.equals(c2)) {
                return 1;
            }
            if (Boolean.TYPE.equals(c1)) {
                if(c2.equals(Boolean.TYPE)){
                    return 2;
                }
            }
            if (Byte.TYPE.equals(c1)) {
                if(c2.equals(Byte.TYPE)){
                    return 2;
                }
            }
            if (Short.TYPE.equals(c1)) {
                if(c2.equals(Byte.TYPE) || c2.equals(Short.TYPE)){
                    return 2;
                }
            }
            if (Character.TYPE.equals(c1)) {
                if(c2.equals(Character.TYPE)){
                    return 2;
                }
            }
            if (Integer.TYPE.equals(c1)) {
                if(c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE)){
                    return 2;
                }
            }
            if (Long.TYPE.equals(c1)) {
                if(c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE)){
                    return 2;
                }
            }
            if (Float.TYPE.equals(c1)) {
                if(c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE)){
                    return 2;
                }
            }
            if (Double.TYPE.equals(c1)) {
                if(c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE) || c2.equals(Float.TYPE) || c2.equals(Double.TYPE)){
                    return 2;
                }
            }
        }
        return -1;
    }

    public static Class firstCommonSuperType(Class type1, Class type2) {
        if (type1 == null || type2 == null) {
            return Object.class;
        }
        if (type1.equals(Object.class) || type2.equals(Object.class)) {
            return Object.class;
        }
        if (type1.equals(type2)) {
            return type1;
        }
        if (type1.isAssignableFrom(type2)) {
            return type1;
        }
        if (type2.isAssignableFrom(type1)) {
            return type2;
        }
        return firstCommonSuperType(type1.getSuperclass(), type2);
    }

    public static class MethodSignature {

        private final Class[] types;
        private final boolean varArgs;

        public MethodSignature(Class[] types, boolean varArgs) {
            this.types = types;
            this.varArgs = varArgs;
            if (varArgs) {
                if (types.length == 0) {
                    throw new IllegalArgumentException("Expected Array");
                }
                if (!types[types.length - 1].isArray()) {
                    throw new IllegalArgumentException("Expected Array");
                }
            }
        }

        public Class[] getTypes() {
            return Arrays.copyOf(types, types.length);
        }

        public boolean isVarArgs() {
            return varArgs;
        }

    }

    public static class MethodObject<T> {
        private MethodSignature signature;
        private T method;

        public MethodObject(MethodSignature signature, T method) {
            this.signature = signature;
            this.method = method;
        }

        public MethodSignature getSignature() {
            return signature;
        }

        public T getMethod() {
            return method;
        }
    }

    public static <T> MethodObject<T> getMatchingMethod(MethodObject<T>[] available, Class... parameterTypes) {
        class MCall implements Comparable<MCall> {

            Class[] input;
            MethodObject<T> av;
            int[] cost;

            public MCall(Class[] input, MethodObject<T> av, int[] cost) {
                this.input = input;
                this.av = av;
                this.cost = cost;
            }

            @Override
            public int compareTo(MCall o) {
                int x = Integer.compare(cost.length, o.cost.length);
                if (x != 0) {
                    return x;
                }
                for (int i = 0; i < cost.length; i++) {
                    int val1 = cost[i];
                    int val2 = o.cost[i];
                    x = Integer.compare(val1, val2);
                    if (x != 0) {
                        return x;
                    }
                }
                return 0;
            }
        }

        //List<MCall> valid = new ArrayList<MCall>();
        MCall best = null;
        for (int i = 0; i < available.length; i++) {
            MethodObject<T> a = available[i];
            Class[] atypes = a.signature.getTypes();
            Class[] itypes = parameterTypes;
            if (atypes.length == itypes.length && !a.signature.isVarArgs()) {
                boolean ok = true;
                int[] cost = new int[atypes.length];
                for (int j = 0; j < atypes.length; j++) {
                    Class c1 = atypes[j];
                    Class c2 = itypes[j];
                    cost[j] = getAssignationCost(c1, c2);
                    if (cost[j] < 0) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    //check each type
                    MCall newCall = new MCall(parameterTypes, a, cost);
                    if (best == null || best.compareTo(newCall) > 0) {
                        best = newCall;
                    }
                }
            } else if (atypes.length < itypes.length && a.signature.isVarArgs()) {
                boolean ok = true;
                Class[] r = new Class[atypes.length];
                int[] cost = new int[atypes.length];
                for (int j = 0; j < atypes.length - 1; j++) {
                    Class c1 = atypes[j];
                    Class c2 = itypes[j];
                    cost[j] = getAssignationCost(c1, c2);
                    r[j] = c2;
                    if (cost[j] < 0) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    Class ll = atypes[atypes.length - 1].getComponentType();
                    int y = 0;
                    for (int j = atypes.length - 1; j < itypes.length; j++) {
                        Class itype = itypes[j];
                        int c = getAssignationCost(ll, itype);
                        if (c < 0) {
                            ok = false;
                        } else {
                            y += c;
                        }
                    }
                    if (ok) {
                        cost[atypes.length - 1] = y;
                        r[atypes.length - 1] = atypes[atypes.length - 1];
                    }
                }
                if (ok) {
                    MCall newCall = new MCall(r, a, cost);
                    if (best == null || best.compareTo(newCall) > 0) {
                        best = newCall;
                    }
                }
            }
        }
        return best == null ? null : best.av;
    }


    static void setAccessibleWorkaround(AccessibleObject o) {
        if (o == null || o.isAccessible()) {
            return;
        }
        try {
            o.setAccessible(true);
        } catch (SecurityException e) { // NOPMD
            // ignore in favor of subsequent IllegalAccessException
        }
    }


    /**
     * This is a work around if jdk 8 is not available
     * @param d
     * @return
     */
    public static boolean isDoubleFinite(double d) {
        return Math.abs(d) <= Double.MAX_VALUE;
    }

    public static boolean isInt(double d) {
        return ((int) (d)) == d;
//        return Math.floor(d) == d;
    }

}
