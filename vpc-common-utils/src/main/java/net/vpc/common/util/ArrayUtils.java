/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.lang.reflect.Array;

/**
 *
 * @author vpc
 */
public class ArrayUtils {

    public static <T> T[] removeHead(T[] arr, int count) {
        int nc = Math.max(arr.length - count, 0);
        T[] arr2 = (T[]) Array.newInstance(arr.getClass().getComponentType(), nc);
        if (nc > 0) {
            System.arraycopy(arr, count, arr2, 0, nc);
        }
        return arr2;
    }
}
