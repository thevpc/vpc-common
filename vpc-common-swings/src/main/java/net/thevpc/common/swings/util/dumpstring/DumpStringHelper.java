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
package net.thevpc.common.swings.util.dumpstring;

import java.util.StringTokenizer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 17:54:12
 */
public class DumpStringHelper {
    private StringBuilder sb = new StringBuilder();
    private String name;
    private boolean simple;

    public DumpStringHelper(Object c) {
        this(c.getClass().getSimpleName());
    }

    public DumpStringHelper(Object c, boolean simple) {
        this(c.getClass().getSimpleName(),simple);
    }

    public DumpStringHelper(String name) {
        this(name, false);
    }

    public DumpStringHelper(String name, boolean simple) {
        this.name = name == null ? "" : name;
        this.simple = simple;
    }

    public DumpStringHelper add(Object value) {
        return add(null, value);
    }

    public DumpStringHelper add(String varName, Object value) {
        boolean first = sb.length() == 0;
        String p = null;
        if(simple){
            p = varName == null ? "" : (varName + "=");
        }else{
            p = varName == null ? "  " : ("  " + varName + " = ");
        }
//        StringBuilder prefix=new StringBuilder();
//        for (int i = 0; i < p.length(); i++) {
//              prefix.append(' ');
//        }
        if (!first) {
            if (simple) {
                sb.append(";");
            }
        }
        sb.append(p);
        String str = DumpStringUtils.getDumpString(value);
        if (str.indexOf('\n') >= 0 || str.indexOf('\r') >= 0) {
            boolean firstLine = true;
            for (StringTokenizer stok = new StringTokenizer(str, "\r\n"); stok.hasMoreTokens();) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    sb.append("  ");
                }
                sb.append(stok.nextToken()).append("\n");
            }
        } else {
            sb.append(str);
            if (!simple) {
                sb.append("\n");
            }
        }
        return this;
    }

    public String toString() {
        String s = sb.toString();
        if (simple) {
            return name + "(" + s + ")";
        } else {
            if (s.indexOf('\n') >= 0) {
                return name + "{\n" + s + "}";
            } else {
                return name + "{" + s + "}";
            }
        }
    }
}
