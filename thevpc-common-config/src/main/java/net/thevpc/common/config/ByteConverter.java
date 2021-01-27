package net.thevpc.common.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:38
 * To change this template use File | Settings | File Templates.
 */
class ByteConverter extends Converter {
    public ByteConverter() {
        super(Byte.class, "byte");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return new Byte(s);
    }
}
