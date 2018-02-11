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

import net.vpc.common.prs.reflect.FieldFilter;
import net.vpc.common.prs.reflect.Reflector;
import net.vpc.common.prs.xml.XmlSerializationDelegate;
import net.vpc.common.prs.xml.XmlSerializationException;
import net.vpc.common.prs.xml.XmlSerializer;
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