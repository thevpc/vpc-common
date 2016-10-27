package net.vpc.common.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:18
 * To change this template use File | Settings | File Templates.
 */
class LongConverter extends Converter {
    public LongConverter() {
        super(Long.class, "long");
    }

    public String objectToString(Object o) {
        return String.valueOf(o);
    }

    public Object stringToObject(String s) throws ParseException {
        return new Long(s);
    }
}
