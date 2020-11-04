/**
 * ====================================================================
 *            Nuts : Network Updatable Things Service
 *                  (universal package manager)
 * <br>
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
 * <br>
 * Copyright (C) 2016-2020 thevpc
 * <br>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <br>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <br>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.commons.md.doc.util;

/**
 *
 * @author vpc
 */
public class CharReader {

    private StringBuilder sb = new StringBuilder();

    public CharReader(String s) {
        sb.append(s);
    }

    public boolean isEmpty() {
        return sb.length() == 0;
    }

    public char peek() {
        return sb.charAt(0);
    }
    
    public char read() {
        char c = sb.charAt(0);
        sb.delete(0, 1);
        return c;
    }

    public boolean read(String z) {
        if (peek(z)) {
            sb.delete(0, z.length());
            return true;
        }
        return false;
    }

    public boolean peek(String z) {
        if (z == null || z.length() == 0) {
            throw new IllegalArgumentException("empty peek");
        }
        return peek(z.toCharArray());
    }

//    public String readUntil(char[] z) {
//        int i = indexOf(z, 0);
//        if(i<0){
//            
//        }
//    }
    
    public int indexOf(char[] z,int fromIndex) {
        for (int i = fromIndex; i < sb.length()-z.length+1; i++) {
            if(peek(z)){
                return i;
            }
        }
        return -1;
    }
    
    public boolean read(char[] z) {
        if (peek(z)) {
            sb.delete(0, z.length);
            return true;
        }
        return false;
    }

    public boolean peek(char[] z) {
        if (z == null || z.length == 0) {
            throw new IllegalArgumentException("empty peek");
        }
        if (sb.length() >= z.length) {
            for (int i = 0; i < z.length; i++) {
                if (sb.charAt(i) != z[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean peek(char[] z,int fromIndex) {
        if (z == null || z.length == 0) {
            throw new IllegalArgumentException("empty peek");
        }
        if (sb.length()-fromIndex >= z.length) {
            for (int i = 0; i < z.length; i++) {
                if (sb.charAt(i+fromIndex) != z[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean peek(char z) {
        if (sb.length() >= 1) {
            return sb.charAt(0) == z;
        }
        return false;
    }
}
