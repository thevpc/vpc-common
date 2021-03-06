package net.thevpc.common.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:26
 * To change this template use File | Settings | File Templates.
 */
class FloatConverter extends Converter {
    public FloatConverter() {
        super(Float.class, "float");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return new Float(s);
    }
}
