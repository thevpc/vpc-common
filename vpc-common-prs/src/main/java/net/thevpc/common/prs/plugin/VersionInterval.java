/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.thevpc.common.prs.plugin;

import net.thevpc.common.prs.Version;

import java.io.Serializable;
import java.text.ParseException;

/**
 *
 * @author vpc
 */
public class VersionInterval implements Serializable {

    public static final VersionInterval ANY_VERSION = new VersionInterval(null, null, true, true);
    private Version min;
    private Version max;
    private boolean includesMin;
    private boolean includesMax;

    /**
     * never use this constructor
     */
    public VersionInterval() {
    }
//    public static void main(String[] args) {
//        try {
//            System.out.println(VersionIntervall.parse("[0,0.4.6]").toString());
//        } catch (ParseException ex) {
//            Logger.getLogger(VersionIntervall.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public VersionInterval(String str) throws ParseException {
        if (str == null) {
            str = "";
        }
        str = str.trim();
        if ((str.startsWith("[") || str.startsWith("]")) && (str.endsWith("[") || str.endsWith("]"))) {
            includesMin = str.startsWith("[");
            includesMax = str.endsWith("]");
            str = str.substring(1, str.length() - 1);
            int v = str.indexOf(',');
            if (v > 0) {
                String ms = str.substring(0, v).trim();
                if (ms.length() == 0 || ms.equals("*") || ms.equals("-")) {
                    min = null;
                } else {
                    min = new Version(ms);
                }
                String mx = str.substring(v + 1).trim();
                if (mx.length() == 0 || mx.equals("*") || mx.equals("-")) {
                    max = null;
                } else {
                    max = new Version(mx);
                }
            }
        } else if (str.indexOf("[") >= 0) {
            throw new ParseException("Unexpected '[' ", str.indexOf("["));
        } else if (str.indexOf("]") >= 0) {
            throw new ParseException("Unexpected ']'", str.indexOf("["));
        } else {
            throw new ParseException("Bad version intervall", 0);
        }
    }

    public VersionInterval(Version min, Version max, boolean includesMin, boolean includesMax) {
        this.min = min;
        this.max = max;
        this.includesMin = includesMin;
        this.includesMax = includesMax;
    }

    public int compare(Version v) {
        if (min != null) {
            if (includesMin && v.compareTo(min) < 0) {
                return -1;
            }
            if (!includesMin && v.compareTo(min) <= 0) {
                return -1;
            }
        }
        if (max != null) {
            if (includesMax && v.compareTo(max) > 0) {
                return 1;
            }
            if (!includesMax && v.compareTo(max) >= 0) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (includesMin) {
            sb.append('[');
        } else {
            sb.append(']');
        }
        if (min == null) {
            sb.append('*');
        } else {
            sb.append(min);
        }
        sb.append(',');
        if (max == null) {
            sb.append('*');
        } else {
            sb.append(max);
        }
        if (includesMax) {
            sb.append(']');
        } else {
            sb.append('[');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.min != null ? this.min.hashCode() : 0);
        hash = 17 * hash + (this.max != null ? this.max.hashCode() : 0);
        hash = 17 * hash + (this.includesMin ? 1 : 0);
        hash = 17 * hash + (this.includesMax ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VersionInterval other = (VersionInterval) obj;
        if (this.min != other.min && (this.min == null || !this.min.equals(other.min))) {
            return false;
        }
        if (this.max != other.max && (this.max == null || !this.max.equals(other.max))) {
            return false;
        }
        if (this.includesMin != other.includesMin) {
            return false;
        }
        if (this.includesMax != other.includesMax) {
            return false;
        }
        return true;
    }

    public static VersionInterval parse(String s) throws ParseException {
        return (s == null || s.trim().length() == 0) ? ANY_VERSION : new VersionInterval(s);
    }

}
