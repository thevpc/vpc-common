/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.thevpc.common.prs.plugin;

import net.thevpc.common.prs.Version;
import net.thevpc.common.prs.xml.XmlSerializable;
import net.thevpc.common.prs.xml.XmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vpc
 */
public class VersionIntervall implements XmlSerializable {

    public static final VersionIntervall ANY_VERSION = new VersionIntervall(null, null, true, true);
    private Version min;
    private Version max;
    private boolean includesMin;
    private boolean includesMax;

    /**
     * never use this constructor
     */
    public VersionIntervall() {
    }
//    public static void main(String[] args) {
//        try {
//            System.out.println(VersionIntervall.parse("[0,0.4.5]").toString());
//        } catch (ParseException ex) {
//            Logger.getLogger(VersionIntervall.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public VersionIntervall(String str) throws ParseException {
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

    public VersionIntervall(Version min, Version max, boolean includesMin, boolean includesMax) {
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
        final VersionIntervall other = (VersionIntervall) obj;
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

    public static VersionIntervall parse(String s) throws ParseException {
        return (s == null || s.trim().length() == 0) ? ANY_VERSION : new VersionIntervall(s);
    }

    public void storeXmlNode(XmlSerializer serializer, Document doc, Element element) {
        element.setAttribute("value", toString());
    }

    public void loadXmlNode(XmlSerializer serializer, Element element) {
        try {
            VersionIntervall ve = parse(element.getAttribute("value"));
            if (ve != null) {
                this.min = ve.min;
                this.max = ve.max;
                this.includesMin = ve.includesMin;
                this.includesMax = ve.includesMax;
            }
        } catch (ParseException ex) {
            Logger.getLogger(VersionIntervall.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
