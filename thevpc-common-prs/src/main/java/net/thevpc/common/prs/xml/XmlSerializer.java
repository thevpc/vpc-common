package net.thevpc.common.prs.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  13 juil. 2006 22:14:21
 */
public interface XmlSerializer {
    public Element createNode(Document doc, String name, Object value);
    public Object createObject(Element value) throws XmlSerializationException;

    public Object xmlToObject(InputStream in) throws IOException;
    public void objectToXml(Object o, OutputStream stream) throws IOException ;
}
