package net.thevpc.common.config;

import java.awt.*;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:55
 * To change this template use File | Settings | File Templates.
 */
class FontConverter extends Converter {
    public FontConverter() {
        super(Font.class, "font");
    }

    public String objectToString(Object o) {
        return ConfigurationImplHelper.getStringFromFont((Font) o);
    }

    public Object stringToObject(String s) throws ParseException {
        return ConfigurationImplHelper.getFontFromString(s);
    }
}
