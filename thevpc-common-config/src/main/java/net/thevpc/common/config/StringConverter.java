package net.thevpc.common.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:21:02
 * To change this template use File | Settings | File Templates.
 */
class StringConverter extends Converter {
    public StringConverter() {
        super(String.class, "string");
    }

    public String objectToString(Object o) {
        return (String) o;
    }

    public Object stringToObject(String s) throws ParseException {
        return s;
    }
}
