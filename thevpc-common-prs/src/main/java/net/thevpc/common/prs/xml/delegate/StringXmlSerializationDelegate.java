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
package net.thevpc.common.prs.xml.delegate;

import net.thevpc.common.prs.xml.XmlSerializationDelegate;
import net.thevpc.common.prs.xml.XmlSerializationException;
import net.thevpc.common.prs.xml.XmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 4 janv. 2006 15:13:28
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