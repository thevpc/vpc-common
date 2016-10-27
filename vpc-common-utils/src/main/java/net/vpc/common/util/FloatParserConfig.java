/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.util;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class FloatParserConfig {

    public static final FloatParserConfig STRICT = new FloatParserConfig().setErrorIfInvalid(true).setErrorIfNull(true);
    public static final FloatParserConfig LENIENT = new FloatParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(0).setInvalidValue(Float.NaN);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private float nullValue = Float.NaN;
    private float invalidValue = Float.NaN;

    private FloatParserConfig() {
    }

    
    public boolean isErrorIfNull() {
        return errorIfNull;
    }

    public boolean isErrorIfInvalid() {
        return errorIfInvalid;
    }

    public float getNullValue() {
        return nullValue;
    }

    public float getInvalidValue() {
        return invalidValue;
    }

    public FloatParserConfig setErrorIfNull(boolean errorIfNull) {
        FloatParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public FloatParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        FloatParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public FloatParserConfig setNullValue(float defaultNullValue) {
        FloatParserConfig x = copy();
        x.nullValue = defaultNullValue;
        return x;
    }

    public FloatParserConfig setInvalidValue(float defaultInvalidValue) {
        FloatParserConfig x = copy();
        x.invalidValue = defaultInvalidValue;
        return x;
    }

    private FloatParserConfig copy() {
        FloatParserConfig t = new FloatParserConfig();
        t.errorIfInvalid = errorIfInvalid;
        t.errorIfNull = errorIfNull;
        t.nullValue = nullValue;
        t.invalidValue = invalidValue;
        return t;
    }

}
