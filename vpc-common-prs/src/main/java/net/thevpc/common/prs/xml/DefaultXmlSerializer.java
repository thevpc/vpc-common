package net.thevpc.common.prs.xml;

import net.thevpc.common.prs.util.ClassMap;
import net.thevpc.common.prs.xml.delegate.URLXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.ClassXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.StringXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.UUIDXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.CollectionXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.NumberXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.DateXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.FileXmlSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.JavaSerializationDelegate;
import net.thevpc.common.prs.xml.delegate.MapXmlSerializationDelegate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class DefaultXmlSerializer implements XmlSerializer {
    private ClassMap<XmlSerializationDelegate> declaredHandlers = new ClassMap<XmlSerializationDelegate>();
    private int nextRefId = 1;
    private JavaSerializationDelegate javaSerializationDelegate = new JavaSerializationDelegate();
    private HashMap<Integer, Object> readReferences = new HashMap<Integer, Object>();
    private HashMap<InternalObjectRef, Element> writeReferences = new HashMap<InternalObjectRef, Element>();

    private ClassLoader defaultClassLoader;

    private static class InternalObjectRef {
        private Object object;

        public InternalObjectRef(Object o) {
            this.object = o;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof InternalObjectRef) {
                InternalObjectRef oo = (InternalObjectRef) obj;
                return oo.object == object;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return object.hashCode();
        }
    }

    public DefaultXmlSerializer() {
        setHandler(Class.class, new ClassXmlSerializationDelegate());
        setHandler(Collection.class, new CollectionXmlSerializationDelegate());
        setHandler(Map.class, new MapXmlSerializationDelegate());
        setHandler(File.class, new FileXmlSerializationDelegate());
        setHandler(URL.class, new URLXmlSerializationDelegate());
        setHandler(java.util.Date.class, new DateXmlSerializationDelegate());
        setHandler(java.lang.Number.class, new NumberXmlSerializationDelegate());
        setHandler(String.class, new StringXmlSerializationDelegate());
        setHandler(UUID.class, new UUIDXmlSerializationDelegate());
    }

    public final void setHandler(Class clazz, XmlSerializationDelegate delegate) {
        if (delegate != null) {
            declaredHandlers.put(clazz, delegate);
        } else {
            declaredHandlers.remove(clazz);
        }
    }

    public XmlSerializationDelegate getHandler(Class clazz) {
        XmlSerializationDelegate best = declaredHandlers.getBest(clazz);
        if (best == null) {
            throw new XmlSerializationHandlerNotFoundException(clazz.getName());
        }
        return best;
    }

    public Element createNode(Document doc, String name, Object value) {
        Element element = doc.createElement(name);
        if (value == null) {
            element.setAttribute("type", "null");
            return element;
        }
        InternalObjectRef ref = new InternalObjectRef(value);
        if (
                !(value instanceof Boolean)
//                        && !(value instanceof String)
                        && !(value instanceof Character)
                        && !(value instanceof Number)
                        && !(value.getClass().isPrimitive())
                        && writeReferences.containsKey(ref)
                ) {
            int refId = 0;
            try {
                refId = Integer.valueOf(writeReferences.get(ref).getAttribute("id"));
            } catch (Throwable e) {
                //
            }
            if (refId == 0) {
                refId = nextRefId;
                writeReferences.get(ref).setAttribute("id", String.valueOf(refId));
                ++nextRefId;
            }
            element.setAttribute("type", "#ref");
            element.setAttribute("value", String.valueOf(refId));
            nextRefId++;
        } else if (value.getClass().isArray()) {
            element.setAttribute("type", "array");
            element.setAttribute("elementType", value.getClass().getComponentType().getName());
            element.setAttribute("length", String.valueOf(Array.getLength(value)));
            for (int i = 0; i < Array.getLength(value); i++) {
                element.appendChild(createNode(doc, "item" + i, Array.get(value, i)));
            }
        } else {
            writeReferences.put(ref, element);
            if (value instanceof Integer) {
                element.setAttribute("type", "int");
                element.setAttribute("value", Integer.toString((Integer) value));
            } else if (value instanceof Long) {
                element.setAttribute("type", "long");
                element.setAttribute("value", Long.toString((Long) value));
            } else if (value instanceof Short) {
                element.setAttribute("type", "long");
                element.setAttribute("value", Short.toString((Short) value));
            } else if (value instanceof Float) {
                element.setAttribute("type", "float");
                element.setAttribute("value", Float.toString((Float) value));
            } else if (value instanceof Double) {
                element.setAttribute("type", "double");
                element.setAttribute("value", Double.toString((Double) value));
            } else if (value instanceof Byte) {
                element.setAttribute("type", "byte");
                element.setAttribute("value", Byte.toString((Byte) value));
            } else if (value instanceof Character) {
                element.setAttribute("type", "char");
                element.setAttribute("value", Character.toString((Character) value));
            } else if (value instanceof Boolean) {
                element.setAttribute("type", "boolean");
                element.setAttribute("value", Boolean.toString((Boolean) value));
            } else {
                String className = value.getClass().getName();
                element.setAttribute("type", className.startsWith("java.lang.") ?
                        className.substring("java.lang.".length())
                        : className.startsWith("java.util.") ? className.substring("java.util.".length())
                        : className
                );
                if (value instanceof XmlSerializable) {
                    ((XmlSerializable) value).storeXmlNode(this, doc, element);
                    return element;
                } else {
                    Class clzz = value.getClass();
                    XmlSerializationDelegate best = declaredHandlers.getBest(clzz);
                    if (best != null) {
                        best.store(this, doc, element, value);
                    } else if (Serializable.class.isAssignableFrom(clzz)) {
                        javaSerializationDelegate.store(this, doc, element, value);
                    } else {
                        throw new XmlSerializationHandlerNotFoundException(clzz.getName());
                    }
                }
            }
        }
        return element;
    }

//    private String resolveXmlAttribute(Node value,String name){
//        if(value instanceof Element){
//            return ((Element)value).getAttribute(name);
//        }else{
//            NamedNodeMap attributes = value.getAttributes();
//            attributes.getNamedItem()
//
//        }
//    }

    public Object createObject(Element value) throws XmlSerializationException {
        String type = value.getAttribute("type");
        int refId = 0;
        try {
            refId = Integer.valueOf(value.getAttribute("id"));
        } catch (Throwable e) {
            //
        }
        if ("null".equals(type)) {
            return null;
        } else if ("#ref".equals(type)) {
            Integer ref2 = Integer.valueOf(value.getAttribute("value"));
            return readReferences.get(ref2);
        }
        Object ret;
        if ("int".equals(type)) {
            ret = Integer.parseInt(value.getAttribute("value"));
        } else if ("long".equals(type)) {
            ret = Long.parseLong(value.getAttribute("value"));
        } else if ("short".equals(type)) {
            ret = Short.parseShort(value.getAttribute("value"));
        } else if ("float".equals(type)) {
            ret = Float.parseFloat(value.getAttribute("value"));
        } else if ("double".equals(type)) {
            ret = Double.parseDouble(value.getAttribute("value"));
        } else if ("byte".equals(type)) {
            ret = Byte.parseByte(value.getAttribute("value"));
        } else if ("char".equals(type)) {
            ret = Character.valueOf(value.getAttribute("value").charAt(0));
        } else if ("boolean".equals(type)) {
            ret = Boolean.parseBoolean(value.getAttribute("value"));
        } else if ("array".equals(type)) {
            try {
                final int ln = Integer.parseInt(value.getAttribute("length"));
                ret = Array.newInstance(getClassByName(value.getAttribute("elementType")), ln);
                NodeList childNodes = value.getChildNodes();
                int j = 0;
                for (int i = 0; j < childNodes.getLength() && i < ln; ) {
                    Node node = childNodes.item(j++);
                    if (node instanceof Element) {
                        Array.set(ret, i++, createObject((Element) node));
                    } else {
                        //???
                    }
                }
            } catch (Throwable ex) {
                throw new XmlSerializationException(ex);
                //Logger.getLogger(DefaultXmlSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Class c = getClassByName(type);
                if (XmlSerializable.class.isAssignableFrom(c)) {
                    XmlSerializable xmlObject = (XmlSerializable) c.newInstance();
                    xmlObject.loadXmlNode(this, value);
                    ret = xmlObject;
                } else {
                    XmlSerializationDelegate best = declaredHandlers.getBest(c);
                    if (best != null) {
                        ret = best.create(this, value, c);
                        if (refId > 0) {
                            readReferences.put(refId, ret);
                        }
                        best.load(this, value, c, ret);
                    } else if (Serializable.class.isAssignableFrom(c)) {
                        best = javaSerializationDelegate;
                        ret = best.create(this, value, c);
                        if (refId > 0) {
                            readReferences.put(refId, ret);
                        }
                        best.load(this, value, c, ret);
                    } else {
                        throw new XmlSerializationHandlerNotFoundException(c.getName());
                    }
                }
            } catch (XmlSerializationException e) {
                throw e;
            } catch (Throwable e) {
                throw new XmlSerializationException(e);
            }
        }
        return ret;
    }

    private Class getClassByName(String clz) throws Throwable {
        Throwable exc;
        if (defaultClassLoader == null) {
            try {
                return Class.forName(clz);
            } catch (Throwable e) {
                exc = e;
                try {
                    return Class.forName("java.lang." + clz);
                } catch (Throwable e1) {
                    try {
                        return Class.forName("java.util." + clz);
                    } catch (ClassNotFoundException e2) {
                        //
                    }
                }
            }
        } else {
            try {
                return getDefaultClassLoader().loadClass(clz);
            } catch (Throwable e) {
                exc = e;
                try {
                    return Class.forName("java.lang." + clz);
                } catch (Throwable e1) {
                    try {
                        return Class.forName("java.util." + clz);
                    } catch (ClassNotFoundException e2) {
                        //
                    }
                }
            }
        }
        throw exc;
    }

    public Object xmlToObject(InputStream in) throws IOException {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
//            dbf.setValidating(true);
            dbf.setCoalescing(true);
            dbf.setIgnoringComments(true);
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
//            db.setEntityResolver(new Resolver());
//            db.setErrorHandler(new EH());
                InputSource is = new InputSource(in);
                doc = db.parse(is);
            } catch (ParserConfigurationException x) {
                throw new Error(x);
            }
        } catch (SAXException saxe) {
            throw new InvalidPropertiesFormatException(saxe);
        }
        return createObject((Element) doc.getChildNodes().item(0));
    }

    public void objectToXml(Object o, OutputStream stream) throws IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            assert (false);
        }
        Document doc = db.newDocument();
        doc.appendChild(createNode(doc, "object", o));
        emitDocument(doc, stream);
    }

    private void emitDocument(Document doc, OutputStream os)
            throws IOException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
        } catch (TransformerConfigurationException tce) {
            assert (false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(os);
        try {
            t.transform(doms, sr);
        } catch (TransformerException te) {
            IOException ioe = new IOException();
            ioe.initCause(te);
            throw ioe;
        }
    }

    public ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }

    public void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        this.defaultClassLoader = defaultClassLoader;
    }


}
