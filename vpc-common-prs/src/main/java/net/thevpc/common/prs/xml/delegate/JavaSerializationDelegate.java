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

import net.thevpc.common.prs.reflect.FieldFilter;
import net.thevpc.common.prs.reflect.Reflector;
import net.thevpc.common.prs.xml.XmlSerializationDelegate;
import net.thevpc.common.prs.xml.XmlSerializationException;
import net.thevpc.common.prs.xml.XmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 4 janv. 2006 15:13:28
 */
public class JavaSerializationDelegate implements XmlSerializationDelegate {
    public void store(XmlSerializer xmlSerializer, Document doc, Element element, Object value) {
        Reflector.ClassInfo classInfo=Reflector.getClassInfo(value.getClass(), new FieldFilter() {
            public boolean accept(Field field) {
                return !Modifier.isTransient(field.getModifiers())
                        && !Modifier.isStatic(field.getModifiers())
                        && !Modifier.isFinal(field.getModifiers())
                        && !Modifier.isVolatile(field.getModifiers())
                        ;
            }
        }, null, Reflector.FIELDS);
        for (Map.Entry<String, Field> entry : getFieldsMap(classInfo.getFields()).entrySet()) {
            Field field = entry.getValue();
            field.setAccessible(true);
            Element ee = null;
            try {
                ee = xmlSerializer.createNode(doc, entry.getKey(), field.get(value));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            element.appendChild(ee);
        }
    }

    public Object create(XmlSerializer xmlSerializer, Element element, Class clazz) throws XmlSerializationException {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Throwable e) {
            throw new XmlSerializationException(e);
        }
    }

    public void load(XmlSerializer xmlSerializer, Element element, Class clazz, Object instance) throws XmlSerializationException {
        try {
            Reflector.ClassInfo info=Reflector.getClassInfo(clazz, new FieldFilter() {
                public boolean accept(Field field) {
                    return !Modifier.isTransient(field.getModifiers());
                }
            }, null,Reflector.FIELDS);
            Map<String, Field> fieldsMap = getFieldsMap(info.getFields());
            Map<String, PropertyDescriptor> propsMap = new HashMap<String, PropertyDescriptor>();
            BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (writeMethod != null) {
                    propsMap.put(propertyDescriptor.getName(),propertyDescriptor);
                }
            }
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node instanceof Element) {
                    Element child = (Element) node;
                    String nodeName = child.getNodeName();
                    String attributeName = child.getAttribute("attribute-name");
                    if (attributeName != null && attributeName.trim().length()>0) {
                        nodeName = attributeName;
                    }
                    Field field = fieldsMap.get(nodeName);
                    if (field != null) {
                        field.setAccessible(true);
                        Object fv = xmlSerializer.createObject(child);
                        field.set(instance, fv);
                    } else {
                        //look after property
                        PropertyDescriptor propertyDescriptor=propsMap.get(nodeName);
                        if (propertyDescriptor != null) {
                            Object fv = xmlSerializer.createObject(child);
                            propertyDescriptor.getWriteMethod().invoke(instance, fv);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            throw new XmlSerializationException(e);
        }
    }

    private Map<String, Field> getFieldsMap(List<Field> fields) {
        Map<String, Field> m = new LinkedHashMap<String, Field>();
        TreeSet<String> visited = new TreeSet<String>();
        for (Field field : fields) {
            Field otherField = m.get(field.getName());
            if (otherField != null) {
                m.remove(field.getName());
                m.put(otherField.getDeclaringClass().getName() + "." + otherField.getName(), otherField);
                m.put(field.getDeclaringClass().getName() + "." + field.getName(), field);
            } else if (visited.contains(field.getName())) {
                m.put(field.getDeclaringClass().getName() + "." + field.getName(), field);
            } else {
                visited.add(field.getName());
                m.put(field.getName(), field);
            }
        }
        return m;
    }
}