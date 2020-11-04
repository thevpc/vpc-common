package net.thevpc.common.prs.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public interface XmlSerializable {
    public void storeXmlNode(XmlSerializer serializer, Document doc, Element element);

    public void loadXmlNode(XmlSerializer serializer, Element element);
}
