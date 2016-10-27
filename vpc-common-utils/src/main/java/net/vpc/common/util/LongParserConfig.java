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
public class LongParserConfig {

    public static final LongParserConfig STRICT = new LongParserConfig().setErrorIfInvalid(true).setErrorIfNull(true);
    public static final LongParserConfig LENIENT = new LongParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(0).setInvalidValue(0);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private long nullValue = 0;
    private long invalidValue = 0;

    private LongParserConfig() {
    }

    
    public boolean isErrorIfNull() {
        return errorIfNull;
    }

    public boolean isErrorIfInvalid() {
        return errorIfInvalid;
    }

    public long getNullValue() {
        return nullValue;
    }

    public long getInvalidValue() {
        return invalidValue;
    }

    public LongParserConfig setErrorIfNull(boolean errorIfNull) {
        LongParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public LongParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        LongParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public LongParserConfig setNullValue(long defaultNullValue) {
        LongParserConfig x = copy();
        x.nullValue = defaultNullValue;
        return x;
    }

    public LongParserConfig setInvalidValue(long defaultInvalidValue) {
        LongParserConfig x = copy();
        x.invalidValue = defaultInvalidValue;
        return x;
    }

    private LongParserConfig copy() {
        LongParserConfig t = new LongParserConfig();
        t.errorIfInvalid = errorIfInvalid;
        t.errorIfNull = errorIfNull;
        t.nullValue = nullValue;
        t.invalidValue = invalidValue;
        return t;
    }

}
