package net.thevpc.common.prs.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  4 janv. 2006 00:58:58
 */
public interface XmlSerializationDelegate {
    void store(XmlSerializer xmlSerializer, Document doc, Element element, Object value);

    public Object create(XmlSerializer xmlSerializer, Element element, Class clazz) throws XmlSerializationException ;
    void load(XmlSerializer xmlSerializer, Element element, Class clazz, Object instance) throws XmlSerializationException;
}
