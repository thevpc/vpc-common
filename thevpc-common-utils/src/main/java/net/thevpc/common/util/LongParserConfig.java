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
public class LongParserConfig {

    public static final LongParserConfig STRICT = new LongParserConfig().setErrorIfInvalid(true).setErrorIfNull(true).setNullValue(0L).setInvalidValue(0L);
    public static final LongParserConfig NULL = new LongParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(null).setInvalidValue(null);
    public static final LongParserConfig LENIENT = new LongParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(0L).setInvalidValue(0L);
    public static final LongParserConfig LENIENT_F = new LongParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(-1L).setInvalidValue(-1L);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private Long nullValue = 0L;
    private Long invalidValue = 0L;

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
        if (this.errorIfNull == errorIfNull) {
            return this;
        }
        LongParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public LongParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        if (this.errorIfInvalid == errorIfInvalid) {
            return this;
        }
        LongParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public LongParserConfig setNullValue(Long nullValue) {
        if (Objects.equals(this.nullValue,nullValue)) {
            return this;
        }
        LongParserConfig x = copy();
        x.nullValue = nullValue;
        return x;
    }

    public LongParserConfig setInvalidValue(Long invalidValue) {
        if (Objects.equals(this.invalidValue,invalidValue)) {
            return this;
        }
        LongParserConfig x = copy();
        x.invalidValue = invalidValue;
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
