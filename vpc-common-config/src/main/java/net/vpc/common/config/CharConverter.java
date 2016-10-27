package net.vpc.common.config;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:34
 * To change this template use File | Settings | File Templates.
 */
class CharConverter extends Converter {
    public CharConverter() {
        super(Character.class, "char");
    }

    public String objectToString(Object o) {
        return "" + String.valueOf(o).charAt(0);
    }

    public Object stringToObject(String s) throws ParseException {
        return new Character(s.charAt(0));
    }
}
