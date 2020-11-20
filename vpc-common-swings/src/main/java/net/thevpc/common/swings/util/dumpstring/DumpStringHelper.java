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
package net.thevpc.common.swings.util.dumpstring;

import java.util.StringTokenizer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 1 juin 2007 17:54:12
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
