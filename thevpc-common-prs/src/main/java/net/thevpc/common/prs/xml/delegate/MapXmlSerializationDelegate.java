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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 4 janv. 2006 15:13:28
 */
public class MapXmlSerializationDelegate implements XmlSerializationDelegate {
    public void store(XmlSerializer xmlSerializer, Document doc, Element element, Object value) {
        Map c = (Map) value;
        for (Object o : c.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            Element entryElement = doc.createElement("entry");
            entryElement.appendChild(xmlSerializer.createNode(doc, "key", e.getKey()));
            entryElement.appendChild(xmlSerializer.createNode(doc, "value", e.getValue()));
            element.appendChild(entryElement);
        }
    }

    public Object create(XmlSerializer xmlSerializer, Element element, Class clazz) throws XmlSerializationException {
        try {
            return clazz.newInstance();
        } catch (Throwable e) {
            throw new XmlSerializationException(e);
        }
    }
    public void load(XmlSerializer xmlSerializer, Element element, Class clazz, Object instance) throws XmlSerializationException {
        try {
            Map coll = (Map) instance;
            NodeList childNodes = element.getChildNodes();
            int len = childNodes.getLength();
            for (int i = 0; i < len; i++) {
                Node node = childNodes.item(i);
                if (node instanceof Element) {
                    Element entry = (Element) node;
                    NodeList key = entry.getElementsByTagName("key");
                    NodeList value = entry.getElementsByTagName("value");
                    if(key.getLength()>0 && value.getLength()>0){
                        coll.put(xmlSerializer.createObject((Element) key.item(0)),xmlSerializer.createObject((Element) value.item(0)));
                    }
//                }else{
//                    System.out.println("node = " + node);
                }
            }
        } catch (Throwable e) {
            throw new XmlSerializationException(e);
        }
    }
}
