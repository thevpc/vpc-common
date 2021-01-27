package net.thevpc.common.prs.xml;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  3 janv. 2006 22:28:14
 */
public class XmlSerializationException extends RuntimeException {
    public XmlSerializationException(String msg) {
        super(msg);
    }

    public XmlSerializationException(Throwable cause) {
        super(cause);
    }
}
