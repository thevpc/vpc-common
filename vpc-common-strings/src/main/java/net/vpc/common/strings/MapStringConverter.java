/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.strings;

import java.util.Map;

/**
 * @author taha.bensalah@gmail.com
 */
public class MapStringConverter implements StringConverter {
    private Map<String, String> values;

    public MapStringConverter(Map<String, String> values) {
        this.values = values;
    }

    @Override
    public String convert(String str) {
        return values.get(str);
    }

}
