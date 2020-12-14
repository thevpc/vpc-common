/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * @author thevpc
 */
public class ArrayUtils {

    public static String[] concatArrays(String[]... arrays) {
        return concatArrays(String.class, arrays);
    }

    public static double[] concatArrays(double[]... arrays) {
        DoubleArrayList all = new DoubleArrayList();
        if (arrays != null) {
            for (double[] v : arrays) {
                if (v != null) {
                    all.addAll(v);
                }
            }
        }
        return all.toArray();
    }

    public static long[] concatArrays(long[]... arrays) {
        LongArrayList all = new LongArrayList();
        if (arrays != null) {
            for (long[] v : arrays) {
                if (v != null) {
                    all.addAll(v);
                }
            }
        }
        return all.toArray();
    }

    public static int[] concatArrays(int[]... arrays) {
        IntArrayList all = new IntArrayList();
        if (arrays != null) {
            for (int[] v : arrays) {
                if (v != null) {
                    all.addAll(v);
                }
            }
        }
        return all.toArray();
    }

    public static <T> T[] concatArrays(Class<T> cls, T[]... arrays) {
        List<T> all = new ArrayList<>();
        if (arrays != null) {
            for (T[] v : arrays) {
                if (v != null) {
                    all.addAll(Arrays.asList(v));
                }
            }
        }
        return all.toArray((T[]) Array.newInstance(cls, all.size()));
    }

    public static <T> T[] filterArray(Class<T> cls, T[] array, Predicate<T> t) {
        List<T> all = new ArrayList<>();
        for (T v : array) {
            if (t == null || t.test(v)) {
                all.add(v);
            }
        }
        return all.toArray((T[]) Array.newInstance(cls, all.size()));
    }

    public static <T> T[] removeHead(T[] arr, int count) {
        int nc = Math.max(arr.length - count, 0);
        T[] arr2 = (T[]) Array.newInstance(arr.getClass().getComponentType(), nc);
        if (nc > 0) {
            System.arraycopy(arr, count, arr2, 0, nc);
        }
        return arr2;
    }

    public static String[] subArray(String[] source, int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if (endIndex > source.length) {
            beginIndex = 0;
        }
        if (beginIndex >= endIndex) {
            return new String[0];
        }
        String[] arr = new String[endIndex - beginIndex];
        System.arraycopy(source, beginIndex, arr, 0, endIndex - beginIndex);
        return arr;
    }


    public static double[] dtimes(double min, double max, int times) {
        double[] d = new double[times];
        if (times == 1) {
            d[0] = min;
        } else {
            double step = (max - min) / (times - 1);
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
        }
        return d;
    }

    public static double[] dtimes(double min, double max, int times, DoublePredicate filter) {
        if (filter == null) {
            return dtimes(min, max, times);
        }
        double[] d = new double[times];
        if (times == 1) {
            d[0] = min;
        } else {
            double step = (max - min) / (times - 1);
            for (int i = 0; i < d.length; i++) {
                double v = min + i * step;
                if (filter.test(v)) {
                    d[i] = v;
                }
            }
        }
        return d;
    }

    public static float[] ftimes(float min, float max, int times) {
        float[] d = new float[times];
        if (times == 1) {
            d[0] = min;
        } else {
            float step = (max - min) / (times - 1);
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
        }
        return d;
    }

    public static long[] ltimes(long min, long max, int times) {
        long[] d = new long[times];
        if (times == 1) {
            d[0] = min;
        } else {
            long step = (max - min) / (times - 1);
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
        }
        return d;
    }

    public static long[] lsteps(long min, long max, long step) {
        int times = (int) Math.abs((max - min) / step) + 1;
        long[] d = new long[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static double dstepsLength(double min, double max, double step) {
        if (step >= 0) {
            if (max < min) {
                return 0;
            }
            return (int) Math.abs((max - min) / step) + 1;
        } else {
            if (min < max) {
                return 0;
            }
            return (int) Math.abs((max - min) / step) + 1;
        }
    }

    public static double dstepsElement(double min, double max, double step, int index) {
        if (step >= 0) {
            if (max < min) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            return min + index * step;
        } else {
            if (min < max) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            return min + index * step;
        }
    }

    public static double[] dsteps(double min, double max) {
        return dsteps(min, max, 1);
    }

    public static double[] dsteps(double min, double max, double step) {
        if (step >= 0) {
            if (max < min) {
                return new double[0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[] d = new double[times];
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
            return d;
        } else {
            if (min < max) {
                return new double[0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[] d = new double[times];
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
            return d;
        }
    }

    public static float[] fsteps(float min, float max, float step) {
        if (max < min) {
            return new float[0];
        }
        int times = (int) Math.abs((max - min) / step) + 1;
        float[] d = new float[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static int[] isteps(int min, int max, int step) {
        if (max < min) {
            return new int[0];
        }
        int times = Math.abs((max - min) / step) + 1;
        int[] d = new int[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static int[] isteps(int min, int max, int step, IntPredicate filter) {
        if (filter == null) {
            return isteps(min, max, step);
        }
        if (max < min) {
            return new int[0];
        }
        int times = Math.abs((max - min) / step) + 1;
        IntArrayList d = new IntArrayList();
        for (int i = 0; i < times; i++) {
            int v = min + i * step;
            if (filter.test(v)) {
                d.add(v);
            }
        }
        return d.toArray();
    }

    public static int[] itimes(int min, int max, int times) {
        int[] d = new int[times];
        if (times == 1) {
            d[0] = min;
        } else {
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * (max - min) / (times - 1);
            }
        }
        return d;
    }

    public static double[] unboxDoubleList(List<Double> c) {
        double[] r = new double[c.size()];
        for (int i = 0; i < r.length; i++) {
            r[i] = c.get(i);
        }
        return r;
    }

    public static int[] unboxIntegerList(List<Integer> c) {
        int[] r = new int[c.size()];
        for (int i = 0; i < r.length; i++) {
            r[i] = c.get(i);
        }
        return r;
    }

    public static double[] subArray1(double[] values, int count, IndexSelectionStrategy sel) {
        switch (sel) {
            case BALANCED: {
                int[] ints = itimes(0, values.length - 1, count);
                double[] xx = new double[ints.length];
                for (int i = 0; i < ints.length; i++) {
                    xx[i] = values[ints[i]];
                }
                return xx;
            }
            case FIRST: {
                double[] xx = new double[count];
                System.arraycopy(values, 0, xx, 0, count);
                return xx;
            }
            case LAST: {
                double[] xx = new double[count];
                System.arraycopy(values, values.length - count, xx, 0, count);
                return xx;
            }
        }
        return null;
    }

    public static int[] subArray1(int[] values, int count, IndexSelectionStrategy sel) {
        switch (sel) {
            case BALANCED: {
                int[] ints = itimes(0, values.length - 1, count);
                int[] xx = new int[ints.length];
                for (int i = 0; i < ints.length; i++) {
                    xx[i] = values[ints[i]];
                }
                return xx;
            }
            case FIRST: {
                int[] xx = new int[count];
                System.arraycopy(values, 0, xx, 0, count);
                return xx;
            }
            case LAST: {
                int[] xx = new int[count];
                System.arraycopy(values, values.length - count, xx, 0, count);
                return xx;
            }
        }
        return null;
    }

    public static double[] dtimes(double min, double max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return subArray1(dtimes(min, max, maxTimes), times, strategy);
    }

    public static int[] itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        return subArray1(itimes(min, max, maxTimes), times, strategy);
    }


    public static Double[] box(double[] c) {
        Double[] r = new Double[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

    public static Double[][] box(double[][] c) {
        Double[][] r = new Double[c.length][];
        for (int i = 0; i < r.length; i++) {
            r[i] = box(c[i]);
        }
        return r;
    }

    public static Double[][][] box(double[][][] c) {
        Double[][][] r = new Double[c.length][][];
        for (int i = 0; i < r.length; i++) {
            r[i] = box(c[i]);
        }
        return r;
    }

    public static Integer[] box(int[] c) {
        Integer[] r = new Integer[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

    public static int[] unbox(Integer[] c) {
        int[] r = new int[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

    public static Long[] boxLongArray(long[] c) {
        Long[] r = new Long[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

    public static long[] unbox(Long[] c) {
        long[] r = new long[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

    public static double[] unbox(Double[] c) {
        double[] r = new double[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

    public static Float[] box(float[] c) {
        Float[] r = new Float[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

    public static float[] unbox(Float[] c) {
        float[] r = new float[c.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i];
        }
        return r;
    }

}
