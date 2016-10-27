package net.vpc.common.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:22
 * To change this template use File | Settings | File Templates.
 */
class ShortConverter extends Converter {
    public ShortConverter() {
        super(Short.class, "short");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return new Short(s);
    }
}
