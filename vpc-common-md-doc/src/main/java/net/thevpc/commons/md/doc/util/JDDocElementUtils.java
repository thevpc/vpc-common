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

import net.thevpc.commons.md.doc.JDDocElement;
import net.thevpc.commons.md.doc.JDDocElementList;
import net.thevpc.commons.md.doc.JDDocElementString;
import net.thevpc.commons.md.doc.JDDocElementXml;

/**
 *
 * @author vpc
 */
public class JDDocElementUtils {

    public static JDDocElement[] toList(JDDocElement e) {
        if(e instanceof JDDocElementList){
            return ((JDDocElementList) e).values();
        }
        return new JDDocElement[]{e};
    }
    
    public static boolean isXmlTag(JDDocElement e, String tag) {
        e=unpack(e);
        if (e == null) {
            return false;
        }
        if (e instanceof JDDocElementXml) {
            JDDocElementXml s = (JDDocElementXml) e;
            return s.getName().equals(tag);
        }
        return false;
    }

    public static boolean isBlank(JDDocElement e) {
        e=unpack(e);
        if (e == null) {
            return true;
        }
        if (e instanceof JDDocElementString) {
            JDDocElementString s = (JDDocElementString) e;
            if (s.value().trim().isEmpty()) {
                return true;
            }
        }
        if (e instanceof JDDocElementList) {
            JDDocElementList li = (JDDocElementList) e;
            JDDocElement[] t = li.values();
            for (JDDocElement jDDocElement : t) {
                if (!isBlank(jDDocElement)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static JDDocElement unpack(JDDocElement e) {
        if (e == null) {
            return null;
        }
        if (e instanceof JDDocElementList) {
            JDDocElementList li = (JDDocElementList) e;
            JDDocElement[] t = li.values();
            if (t.length == 0) {
                return null;
            }
            if (t.length == 1) {
                return unpack(t[0]);
            }
        }
        return e;
    }

}
