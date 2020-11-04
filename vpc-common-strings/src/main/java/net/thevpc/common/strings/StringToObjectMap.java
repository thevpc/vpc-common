/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.strings;

import java.util.Map;

/**
 * @author taha.bensalah@gmail.com
 */
public class StringToObjectMap implements StringToObject {

    private final Map<String, Object> values;

    public StringToObjectMap(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public Object toObject(String str) {
        return values == null ? null : values.get(str);
    }

}
