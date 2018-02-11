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
public class BooleanParserConfig {

    public static final BooleanParserConfig STRICT = new BooleanParserConfig().setErrorIfInvalid(true).setErrorIfNull(true);
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
        BooleanParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public BooleanParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        BooleanParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public BooleanParserConfig setNullValue(Boolean defaultNullValue) {
        BooleanParserConfig x = copy();
        x.nullValue = defaultNullValue;
        return x;
    }

    public BooleanParserConfig setInvalidValue(Boolean defaultInvalidValue) {
        BooleanParserConfig x = copy();
        x.invalidValue = defaultInvalidValue;
        return x;
    }

    public BooleanParserConfig setTrueStringRegexp(String trueStringRegexp) {
        this.trueStringRegexp = trueStringRegexp;
        BooleanParserConfig x = copy();
        x.trueStringRegexp = trueStringRegexp;
        return x;
    }

    public BooleanParserConfig setFalseStringRegexp(String falseStringRegexp) {
        this.falseStringRegexp = falseStringRegexp;
        BooleanParserConfig x = copy();
        x.falseStringRegexp = falseStringRegexp;
        return x;
    }

    public BooleanParserConfig setAcceptInt(boolean acceptInt) {
        this.acceptInt = acceptInt;
        BooleanParserConfig x = copy();
        x.acceptInt = acceptInt;
        return x;
    }

    public BooleanParserConfig setAcceptNumber(boolean acceptNumber) {
        this.acceptNumber = acceptNumber;
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
