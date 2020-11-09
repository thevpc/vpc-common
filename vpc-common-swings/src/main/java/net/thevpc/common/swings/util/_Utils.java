/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.swings.util;

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