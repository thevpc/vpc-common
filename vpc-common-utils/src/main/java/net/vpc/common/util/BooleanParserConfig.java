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
public class BooleanParserConfig {

    public static final BooleanParserConfig STRICT = new BooleanParserConfig().setErrorIfInvalid(true).setErrorIfNull(true).setNullValue(false).setInvalidValue(false);
    public static final BooleanParserConfig NULL = new BooleanParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(null).setInvalidValue(null);
    public static final BooleanParserConfig LENIENT = new BooleanParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(false).setInvalidValue(false);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private Boolean nullValue = false;
    private Boolean invalidValue = false;
    private String trueStringRegexp = null;
    private String falseStringRegexp = null;
    private boolean acceptInt = true;
    private boolean acceptNumber = true;

    private BooleanParserConfig() {
    }

    public boolean isErrorIfNull() {
        return errorIfNull;
    }

    public boolean isErrorIfInvalid() {
        return errorIfInvalid;
    }

    public boolean getNullValue() {
        return nullValue;
    }

    public boolean getInvalidValue() {
        return invalidValue;
    }

    public BooleanParserConfig setErrorIfNull(boolean errorIfNull) {
        if (this.errorIfNull == errorIfNull) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public BooleanParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        if (this.errorIfInvalid == errorIfInvalid) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public BooleanParserConfig setNullValue(Boolean nullValue) {
        if (Objects.equals(this.nullValue, nullValue)) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.nullValue = nullValue;
        return x;
    }

    public BooleanParserConfig setInvalidValue(Boolean invalidValue) {
        if (Objects.equals(this.invalidValue, invalidValue)) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.invalidValue = invalidValue;
        return x;
    }

    public BooleanParserConfig setTrueStringRegexp(String trueStringRegexp) {
        if (Objects.equals(this.trueStringRegexp, trueStringRegexp)) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.trueStringRegexp = trueStringRegexp;
        return x;
    }

    public BooleanParserConfig setFalseStringRegexp(String falseStringRegexp) {
        if (Objects.equals(this.falseStringRegexp, falseStringRegexp)) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.falseStringRegexp = falseStringRegexp;
        return x;
    }

    public BooleanParserConfig setAcceptInt(boolean acceptInt) {
        if (this.acceptInt == acceptInt) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.acceptInt = acceptInt;
        return x;
    }

    public BooleanParserConfig setAcceptNumber(boolean acceptNumber) {
        if (this.acceptNumber == acceptNumber) {
            return this;
        }
        BooleanParserConfig x = copy();
        x.acceptNumber = acceptNumber;
        return x;
    }

    public String getTrueStringRegexp() {
        return trueStringRegexp;
    }

    public String getFalseStringRegexp() {
        return falseStringRegexp;
    }

    public boolean isAcceptInt() {
        return acceptInt;
    }

    public boolean isAcceptNumber() {
        return acceptNumber;
    }

    
    private BooleanParserConfig copy() {
        BooleanParserConfig t = new BooleanParserConfig();
        t.errorIfInvalid = errorIfInvalid;
        t.errorIfNull = errorIfNull;
        t.nullValue = nullValue;
        t.invalidValue = invalidValue;
        t.acceptInt = acceptInt;
        t.acceptNumber = acceptNumber;
        t.trueStringRegexp = trueStringRegexp;
        t.falseStringRegexp = falseStringRegexp;
        return t;
    }

}
