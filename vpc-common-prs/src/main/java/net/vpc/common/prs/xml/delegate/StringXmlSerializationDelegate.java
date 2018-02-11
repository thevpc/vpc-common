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
package net.vpc.common.prs.xml.delegate;

import net.vpc.common.prs.xml.XmlSerializationDelegate;
import net.vpc.common.prs.xml.XmlSerializationException;
import net.vpc.common.prs.xml.XmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 4 janv. 2006 15:13:28
 */
public class StringXmlSerializationDelegate implements XmlSerializationDelegate {
    public void store(XmlSerializer xmlSerializer, Document doc, Element element, Object value) {
        String c = (String) value;
        if (isSimple(c)) {
            element.setAttribute("value", c);
        } else {
            element.setAttribute("cdata", "true");
            element.appendChild(doc.createCDATASection(c));
        }
    }

    public Object create(XmlSerializer xmlSerializer, Element element, Class clazz) throws XmlSerializationException {
        try {
            if (Boolean.parseBoolean(element.getAttribute("cdata"))) {
                return element.getTextContent();
            }
            return element.getAttribute("value");
        } catch (Throwable e) {
            throw new XmlSerializationException(e);
        }
    }

    public void load(XmlSerializer xmlSerializer, Element element, Class clazz, Object instance) throws XmlSerializationException {

    }

    private boolean isSimple(String s) {
        int max = s.length();
        if (max > 100) {
            return false;
        }
        for (int i = 0; i < max; i++) {
            char c = s.charAt(i);
            if (c == '\n' || c == '\r') {
                return false;
            }
        }
        return true;
    }
}