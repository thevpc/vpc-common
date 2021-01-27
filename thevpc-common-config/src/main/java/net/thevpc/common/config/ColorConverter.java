package net.thevpc.common.config;

import java.awt.*;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:20:58
 * To change this template use File | Settings | File Templates.
 */
class ColorConverter extends Converter {
    public ColorConverter() {
        super(Color.class, "color");
    }

    public String objectToString(Object o) {
        return ConfigurationImplHelper.getStringFromColor((Color) o);
    }

    public Object stringToObject(String s) throws ParseException {
        return ConfigurationImplHelper.getColorFromString(s);
    }
}
