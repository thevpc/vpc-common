/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.util;

import java.util.Objects;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class FloatParserConfig {

    public static final FloatParserConfig STRICT = new FloatParserConfig().setErrorIfInvalid(true).setErrorIfNull(true).setNullValue(0F).setInvalidValue(Float.NaN);
    public static final FloatParserConfig NULL = new FloatParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(null).setInvalidValue(null);
    public static final FloatParserConfig LENIENT = new FloatParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(0F).setInvalidValue(Float.NaN);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private Float nullValue = Float.NaN;
    private Float invalidValue = Float.NaN;

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
        if (this.errorIfInvalid == errorIfInvalid) {
            return this;
        }
        FloatParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public FloatParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        if (this.errorIfInvalid == errorIfInvalid) {
            return this;
        }
        FloatParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public FloatParserConfig setNullValue(Float defaultNullValue) {
        if (Objects.equals(this.nullValue, nullValue)) {
            return this;
        }
        FloatParserConfig x = copy();
        x.nullValue = defaultNullValue;
        return x;
    }

    public FloatParserConfig setInvalidValue(Float defaultInvalidValue) {
        if (Objects.equals(this.nullValue, nullValue)) {
            return this;
        }
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
