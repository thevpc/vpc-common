/*
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 25 mars 03
 * Time: 14:24:29
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package net.vpc.common.swings.util;

public class SubString {
    private String string;
    private String portion;
    private Object evaluatedValue;
    private int offset;
    private int length;
    private int type;

    public SubString(String string, int offset, int length) {
        this(string, offset, length, -1, null);
        this.evaluatedValue = portion;
    }

    public SubString(String string, int offset, int length, int type) {
        this(string, offset, length, type, null);
        this.evaluatedValue = portion;
    }

    public SubString(String string, int offset, int length, Object evaluatedValue) {
        this(string, offset, length, -1, evaluatedValue);
    }

    public SubString(String string, int offset, int length, int type, Object evaluatedValue) {
        this.string = string;
        this.offset = offset;
        this.length = length;
        this.type = type;
        this.portion = string.substring(offset, offset + length);
        this.evaluatedValue = evaluatedValue;
    }

    public Object getEvaluatedValue() {
        return evaluatedValue;
    }

    public String getString() {
        return string;
    }

    public int getOffset() {
        return offset;
    }

    public int length() {
        return length;
    }

    public String substring(int beginIndex, int endIndex) {
        return portion.substring(beginIndex, endIndex);
    }

    public String middle(int startsIgnored, int endsIgnored) {
        return portion.substring(startsIgnored, portion.length() - endsIgnored);
    }

    public String substring(int beginIndex) {
        return portion.substring(beginIndex);
    }

    public String getPortion() {
        return portion;
    }

    public String toString() {
        return portion;
    }

    public int getType() {
        return type;
    }

}
