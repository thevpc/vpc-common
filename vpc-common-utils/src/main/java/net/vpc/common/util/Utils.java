/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.util.*;

/**
 * @author taha.bensalah@gmail.com
 */
public class Utils {

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
        if (filter != null) {
            for (Iterator<T> i = values.iterator(); i.hasNext(); ) {
                if (filter.accept(i.next())) {
                    i.remove();
                }
            }
        }
        return values;
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
}
