package net.thevpc.common.prs.xml;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  4 janv. 2006 15:25:00
 */
public class XmlSerializationHandlerNotFoundException extends RuntimeException {
    public XmlSerializationHandlerNotFoundException() {
    }

    public XmlSerializationHandlerNotFoundException(String message) {
        super(message);
    }

    public XmlSerializationHandlerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlSerializationHandlerNotFoundException(Throwable cause) {
        super(cause);
    }
}
