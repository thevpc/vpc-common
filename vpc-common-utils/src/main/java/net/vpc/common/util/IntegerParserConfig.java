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
public class IntegerParserConfig {

    public static final IntegerParserConfig STRICT = new IntegerParserConfig().setErrorIfInvalid(true).setErrorIfNull(true).setNullValue(0).setInvalidValue(0);
    public static final IntegerParserConfig NULL = new IntegerParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(null).setInvalidValue(null);
    public static final IntegerParserConfig LENIENT = new IntegerParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(0).setInvalidValue(0);
    public static final IntegerParserConfig LENIENT_F = new IntegerParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(-1).setInvalidValue(-1);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private Integer nullValue = 0;
    private Integer invalidValue = 0;

    private IntegerParserConfig() {
    }

    public boolean isErrorIfNull() {
        return errorIfNull;
    }

    public boolean isErrorIfInvalid() {
        return errorIfInvalid;
    }

    public Integer getNullValue() {
        return nullValue;
    }

    public Integer getInvalidValue() {
        return invalidValue;
    }

    public IntegerParserConfig setErrorIfNull(boolean errorIfNull) {
        if (this.errorIfNull == errorIfNull) {
            return this;
        }
        IntegerParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public IntegerParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        if (this.errorIfInvalid == errorIfInvalid) {
            return this;
        }
        IntegerParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public IntegerParserConfig setNullValue(Integer nullValue) {
        if (Objects.equals(this.nullValue, nullValue)) {
            return this;
        }
        IntegerParserConfig x = copy();
        x.nullValue = nullValue;
        return x;
    }

    public IntegerParserConfig setInvalidValue(Integer defaultInvalidValue) {
        if (Objects.equals(this.invalidValue, invalidValue)) {
            return this;
        }
        IntegerParserConfig x = copy();
        x.invalidValue = defaultInvalidValue;
        return x;
    }

    private IntegerParserConfig copy() {
        IntegerParserConfig t = new IntegerParserConfig();
        t.errorIfInvalid = errorIfInvalid;
        t.errorIfNull = errorIfNull;
        t.nullValue = nullValue;
        t.invalidValue = invalidValue;
        return t;
    }

}
