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
public class DoubleParserConfig {

    public static final DoubleParserConfig STRICT = new DoubleParserConfig().setErrorIfInvalid(true).setErrorIfNull(true);
    public static final DoubleParserConfig LENIENT = new DoubleParserConfig().setErrorIfInvalid(false).setErrorIfNull(false).setNullValue(0).setInvalidValue(Double.NaN);
    private boolean errorIfNull = true;
    private boolean errorIfInvalid = true;
    private double nullValue = Double.NaN;
    private double invalidValue = Double.NaN;

    private DoubleParserConfig() {
    }

    
    public boolean isErrorIfNull() {
        return errorIfNull;
    }

    public boolean isErrorIfInvalid() {
        return errorIfInvalid;
    }

    public double getNullValue() {
        return nullValue;
    }

    public double getInvalidValue() {
        return invalidValue;
    }

    public DoubleParserConfig setErrorIfNull(boolean errorIfNull) {
        DoubleParserConfig x = copy();
        x.errorIfNull = errorIfNull;
        return x;
    }

    public DoubleParserConfig setErrorIfInvalid(boolean errorIfInvalid) {
        DoubleParserConfig x = copy();
        x.errorIfInvalid = errorIfInvalid;
        return x;
    }

    public DoubleParserConfig setNullValue(double defaultNullValue) {
        DoubleParserConfig x = copy();
        x.nullValue = defaultNullValue;
        return x;
    }

    public DoubleParserConfig setInvalidValue(double defaultInvalidValue) {
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
