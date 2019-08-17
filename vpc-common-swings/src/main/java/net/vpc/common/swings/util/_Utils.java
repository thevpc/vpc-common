/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.swings.util;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public final class _Utils {
    private _Utils() {
    }

//    public static <T> T dereference(Object o) {
//        if(o!=null && o instanceof ObjectFactory){
//            return ((ObjectFactory<T>)o).create();
//        }
//        return (T) o;
//    }
    
    public static boolean isSet(int value, int flag) {
        return isSet(value, flag, flag);
    }

    public static boolean equals(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static boolean isSet(int value, int flag, int mask) {
        return (value & mask) == flag;
    }

    public static boolean isSetMask(int value, int flag) {
        return (value & flag) != 0;
    }

    public static int set(int value, int flag) {
        return value | flag;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}