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
 * @creationtime  4 janv. 2006 15:13:28
 */
public class ClassXmlSerializationDelegate implements XmlSerializationDelegate {
    public void store(XmlSerializer xmlSerializer, Document doc, Element element, Object value) {
        Class c = (Class) value;
        element.setAttribute("value", c.getName());
    }

    public Object create(XmlSerializer xmlSerializer, Element element, Class clazz) throws XmlSerializationException {
        try {
            String className=element.getAttribute("value");
            return Class.forName(className);
        } catch (Throwable e) {
            throw new XmlSerializationException(e);
        }
    }

    public void load(XmlSerializer xmlSerializer, Element element, Class clazz, Object instance) throws XmlSerializationException {
    }
}
