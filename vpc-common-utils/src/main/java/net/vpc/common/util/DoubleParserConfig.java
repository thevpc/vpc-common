/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.util.Objects;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class DoubleParserConfig {

    public static final DoubleParserConfig STRICT = new DoubleParserConfig().setErrorIfInvalid(true).setErrorIfNull(true).setNullValue(0D).setInvalidValue(Double.NaN);
    public static final DoubleParserConfig NULL = new DoubleParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(null).setInvalidValue(null);
    public static final DoubleParserConfig LENIENT = new DoubleParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(0.0).setInvalidValue(Double.NaN);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private Double nullValue = Double.NaN;
    private Double invalidValue = Double.NaN;

    private DoubleParserConfig() {
    }

    public boolean isErrorIfNull() {
        return errorIfNull;
    }

    public boolean isErrorIfInvalid() {
        return errorIfInvalid;
    }

    public Double getNullValue() {
        return nullValue;
    }

    public Double getInvalidValue() {
        return invalidValue;
    }

    public DoubleParserConfig setErrorIfNull(boolean errorIfNull) {
        if (this.errorIfInvalid == errorIfNull) {
            return this;
        }
        DoubleParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public DoubleParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        if (this.errorIfInvalid == errorIfInvalid) {
            return this;
        }
        DoubleParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public DoubleParserConfig setNullValue(Double defaultNullValue) {
        if (Objects.equals(this.nullValue, defaultNullValue)) {
            return this;
        }
        DoubleParserConfig x = copy();
        x.nullValue = defaultNullValue;
        return x;
    }

    public DoubleParserConfig setInvalidValue(Double defaultInvalidValue) {
        if (Objects.equals(this.nullValue, defaultInvalidValue)) {
            return this;
        }
        DoubleParserConfig x = copy();
        x.invalidValue = defaultInvalidValue;
        return x;
    }

    private DoubleParserConfig copy() {
        DoubleParserConfig t = new DoubleParserConfig();
        t.errorIfInvalid = errorIfInvalid;
        t.errorIfNull = errorIfNull;
        t.nullValue = nullValue;
        t.invalidValue = invalidValue;
        return t;
    }

}
