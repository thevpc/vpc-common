/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * @author taha.bensalah@gmail.com
 */
public class Utils {

    public static int compare(Object a, Object b) {
        return compare(a, b,null);
    }

    public static int compare(Object a, Object b, Comparator c) {
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return -1;
        } else if (b == null) {
            return 1;
        } else if (c != null) {
            return c.compare(a, b);
        } else {
            if (a instanceof Comparable) {
                return ((Comparable) a).compareTo(b);
            }
            if (b instanceof Comparable) {
                return -((Comparable) b).compareTo(a);
            }
            throw new IllegalArgumentException("Not comparable : " + a + "," + b);
        }
    }

    public static Object joinArrays(Object... arrays) {
        Class<?> c1 = null;
        if (arrays.length == 0) {
            throw new IllegalArgumentException("No Array to join");
        }
        int lenSum = 0;
        for (int i = 0; i < arrays.length; i++) {
            Class<?> c = arrays.getClass().getComponentType();
            if (c == null) {
                throw new IllegalArgumentException("Class Cast Exception");
            }
            if (c1 == null) {
                c1 = c;
            } else {
                if (!c1.equals(c)) {
                    throw new IllegalArgumentException("Array expected");
                }
            }
            lenSum += Array.getLength(arrays[i]);
        }
        int pos = 0;
        Object newObj = Array.newInstance(c1, lenSum);
        for (int i = 0; i < arrays.length; i++) {
            int len = Array.getLength(arrays[i]);
            System.arraycopy(arrays[i], 0, newObj, pos, len);
            pos += len;
        }
        return newObj;
    }

    public static Object joinArraysAsType(Class componentType, Object... arrays) {
        if (arrays.length == 0) {
            throw new IllegalArgumentException("No Array to join");
        }
        int lenSum = 0;
        for (int i = 0; i < arrays.length; i++) {
            Class<?> c = arrays.getClass().getComponentType();
            if (!componentType.isInstance(componentType)) {
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

    public static Object copyOfArray(Object array) {
        int len = Array.getLength(array);
        Object newObj = Array.newInstance(array.getClass().getComponentType(), len);
        System.arraycopy(array, 0, newObj, 0, len);
        return newObj;
    }

//    public static void main(String[] args) {
//        Object[] a = (Object[]) removeArray(new Object[]{1, 2, 3}, -1, 2);
//        System.out.println(Arrays.deepToString(a));
//    }
    //    public static Object[] joinArrays(Object[] array1, Object[] array2) {
//        return (Object[]) joinArrays((Object)array1,array2);
//    }
//    public static Object joinArrays(Object array1, Object array2) {
//        Object newArr = Array.newInstance(array1.getClass().getComponentType(), Array.getLength(array1)+Array.getLength(array2));
//        System.arraycopy(array1, 0, newArr, 0, Array.getLength(array1));
//        System.arraycopy(array2, 0, newArr, Array.getLength(array1), Array.getLength(array2));
//        return newArr;
//    }
    public static Object removeArrayTail(Object array, int offset, int removeCount) {
        return removeArray(array, Array.getLength(array) - offset, removeCount);
    }

    public static Object removeArray(Object array, int offset, int removeCount) {
        int len = Array.getLength(array);
        if (offset < 0) {
            removeCount += offset;
            offset = 0;
        }
        if (offset + removeCount > len) {
            removeCount = len - offset;
        }
        if (removeCount > 0) {
            Object newArr = Array.newInstance(array.getClass().getComponentType(), len - removeCount);
            System.arraycopy(array, 0, newArr, 0, offset);
            int srcPos = offset + removeCount - 1;
            int length2 = len - srcPos - 1;
            System.arraycopy(array, srcPos, newArr, offset, length2);
            return newArr;
        } else {
            return copyOfArray(array);
        }
    }

    public static <T, V> V ifType(Object object, Class<T> type, Function<T, V> r) {
        T t = ncast(type, object);
        if (t != null) {
            return r.apply(t);
        }
        return null;
    }

    public static <T> void ncastDo(Class<T> type, Object object, DoWith<T> r) {
        T t = ncast(type, object);
        if (t != null) {
            r.run(t);
        }
    }

    public static <T> T ncast(Class<T> type, Object object) {
        if (type.isInstance(object)) {
            return (T) object;
        }
        return null;
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

    public static long inUseMemory() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory());
    }

    public static long maxFreeMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.maxMemory() - (rt.totalMemory() - rt.freeMemory());
    }

    public static int compare(byte x, byte y) {
        return x - y;
    }

    public static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public static int compare(double d1, double d2) {
        if (d1 < d2) {
            return -1;           // Neither val is NaN, thisVal is smaller
        }
        if (d1 > d2) {
            return 1;            // Neither val is NaN, thisVal is larger
        }
        // Cannot use doubleToRawLongBits because of possibility of NaNs.
        long thisBits = Double.doubleToLongBits(d1);
        long anotherBits = Double.doubleToLongBits(d2);

        return (thisBits == anotherBits ? 0
                : // Values are equal
                (thisBits < anotherBits ? -1
                        : // (-0.0, 0.0) or (!NaN, NaN)
                        1));                          // (0.0, -0.0) or (NaN, !NaN)
    }

    public static int compare(float f1, float f2) {
        if (f1 < f2) {
            return -1;         // Neither val is NaN, thisVal is smaller
        }
        if (f1 > f2) {
            return 1;         // Neither val is NaN, thisVal is larger
        }
        int thisBits = Float.floatToIntBits(f1);
        int anotherBits = Float.floatToIntBits(f2);

        return (thisBits == anotherBits ? 0
                : // Values are equal
                (thisBits < anotherBits ? -1
                        : // (-0.0, 0.0) or (!NaN, NaN)
                        1));                          // (0.0, -0.0) or (NaN, !NaN)
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        @SuppressWarnings("unchecked")
        T[] copy = ((Object) newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static short[] copyOf(short[] original, int newLength) {
        short[] copy = new short[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified array, truncating or padding with zeros (if
     * necessary) so the copy has the specified length. For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain <tt>0</tt>. Such indices will
     * exist if and only if the specified length is greater than that of the
     * original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to
     * obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static int[] copyOf(int[] original, int newLength) {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified array, truncating or padding with zeros (if
     * necessary) so the copy has the specified length. For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain <tt>0L</tt>. Such indices will
     * exist if and only if the specified length is greater than that of the
     * original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to
     * obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static long[] copyOf(long[] original, int newLength) {
        long[] copy = new long[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified array, truncating or padding with null characters
     * (if necessary) so the copy has the specified length. For all indices that
     * are valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain <tt>'\\u000'</tt>. Such indices
     * will exist if and only if the specified length is greater than that of
     * the original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with null
     * characters to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static char[] copyOf(char[] original, int newLength) {
        char[] copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified array, truncating or padding with zeros (if
     * necessary) so the copy has the specified length. For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain <tt>0f</tt>. Such indices will
     * exist if and only if the specified length is greater than that of the
     * original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to
     * obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static float[] copyOf(float[] original, int newLength) {
        float[] copy = new float[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified array, truncating or padding with zeros (if
     * necessary) so the copy has the specified length. For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain <tt>0d</tt>. Such indices will
     * exist if and only if the specified length is greater than that of the
     * original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros to
     * obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static double[] copyOf(double[] original, int newLength) {
        double[] copy = new double[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * Copies the specified array, truncating or padding with <tt>false</tt> (if
     * necessary) so the copy has the specified length. For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values. For any indices that are valid in the copy but
     * not the original, the copy will contain <tt>false</tt>. Such indices will
     * exist if and only if the specified length is greater than that of the
     * original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with false
     * elements to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static boolean[] copyOf(boolean[] original, int newLength) {
        boolean[] copy = new boolean[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    public static <T, V> List<T> sort(List<T> list, final Function<T, V> converter, final Comparator<V> c) {
        if (converter == null) {
            Collections.sort(list, (Comparator<T>) c);
            return list;
        }
        class TandV implements Comparable<TandV> {

            T t;
            V v;

            public TandV(T t) {
                this.t = t;
                this.v = converter.apply(t);
            }

            @Override
            public int compareTo(TandV o) {
                final V v2 = o.v;
                if (v == v2) {
                    return 0;
                }
                if (v == null) {
                    return -1;
                }
                if (v2 == null) {
                    return 1;
                }
                if (c == null) {
                    return ((Comparable) v).compareTo(((Comparable) (v2)));
                }
                return c.compare(v, v2);
            }

        }
        TandV[] all = new TandV[list.size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = new TandV(list.get(i));
        }
        Arrays.sort(all);
        for (int i = 0; i < all.length; i++) {
            list.set(i, all[i].t);
        }
        return list;
    }

    public static <T, V> T[] sort(T[] arr, final Function<T, V> converter, final Comparator<V> c) {
        if (converter == null) {
            Arrays.sort(arr, (Comparator<T>) c);
            return arr;
        }
        class TandV implements Comparable<TandV> {

            T t;
            V v;

            public TandV(T t) {
                this.t = t;
                this.v = converter.apply(t);
            }

            @Override
            public int compareTo(TandV o) {
                final V v2 = o.v;
                if (v == v2) {
                    return 0;
                }
                if (v == null) {
                    return -1;
                }
                if (v2 == null) {
                    return 1;
                }
                if (c == null) {
                    return ((Comparable) v).compareTo(((Comparable) (v2)));
                }
                return c.compare(v, v2);
            }

        }
        TandV[] all = new TandV[arr.length];
        for (int i = 0; i < arr.length; i++) {
            all[i] = new TandV(arr[i]);
        }
        Arrays.sort(all);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = all[i].t;
        }
        return arr;
    }

}
