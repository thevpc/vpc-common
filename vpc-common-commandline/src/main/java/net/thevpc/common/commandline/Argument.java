/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.commandline;

/**
 * @author thevpc
 */
public class Argument {

    private String expression;

    public boolean isOption() {
        return expression != null && expression.startsWith("-");
    }

    boolean isNull() {
        return expression == null;
    }

    public Argument(String expression) {
        this.expression = expression;
    }

    public boolean isNegatedOption() {
        return expression != null &&
                (expression.startsWith("-!") || expression.startsWith("--!"));
    }

    public boolean isNegated() {
        return expression != null &&
                (expression.startsWith("-!") || expression.startsWith("--!") || expression.startsWith("!"));
    }

    public boolean isUnsupportedOption() {
        return expression != null &&
                (expression.startsWith("---") || expression.startsWith("-!!") || expression.startsWith("--!!"));
    }

    public boolean isUnsupported() {
        return expression != null &&
                (expression.startsWith("---") || expression.startsWith("-!!") || expression.startsWith("--!!") || expression.startsWith("!!"));
    }

    public String getName() {
        boolean k = isKeyVal();
        String s = (k) ? getKey() : getExpression();
        if (isUnsupported()) {
            return s;
        }
        if (s != null) {
            if (s.startsWith("!")) {
                return s.substring(1);
            }
            if (s.startsWith("--!")) {
                return "--" + s.substring(3);
            }
            if (s.startsWith("-!")) {
                return "-" + s.substring(2);
            }
        }
        return s;
    }

    public boolean isKeyVal() {
        return expression != null &&
                expression.indexOf('=') >= 0;
    }

    public String getKey() {
        if (isKeyVal()) {
            int x = expression.indexOf('=');
            return expression.substring(0, x);
        }
        throw new IllegalArgumentException("Not a KeyVal");
    }

    public String getOptionName() {
        if (expression != null) {
            if (expression.startsWith("--")) {
                return expression.substring(2);
            } else if (expression.startsWith("-")) {
                return expression.substring(1);
            }
        }
        throw new IllegalArgumentException("Not an option");
    }

    public String getValue() {
        if (isKeyVal()) {
            int x = expression.indexOf('=');
            return expression.substring(x + 1);
        }
        return getExpression();
    }

    public String getStringValue() {
        if (isKeyVal()) {
            return getValue();
        }
        return "";
    }

    public int getIntValue() {
        String value = getStringValue();
        if (value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public long getLongValue() {
        String value = getStringValue();
        if (value.isEmpty()) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public double getDoubleValue() {
        String value = getStringValue();
        if (value.isEmpty()) {
            return 0;
        }
        return Double.parseDouble(value);
    }

    public boolean getBooleanValue() {
        String value = getStringValue();
        if (value.isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean isKeyVal(String sep) {
        return expression != null && expression.contains(sep);
    }

    public String getKey(String sep) {
        if (isKeyVal()) {
            int x = expression.indexOf(sep);
            return expression.substring(0, x);
        }
        throw new IllegalArgumentException("Not a KeyVal");
    }

    public String getValue(String sep) {
        if (isKeyVal()) {
            int x = expression.indexOf(sep);
            return expression.substring(x + sep.length());
        }
        throw new IllegalArgumentException("Not a KeyVal");
    }

    public String getExpression() {
        return expression;
    }

    public String getStringExpression() {
        return getExpression("");
    }

    public String getExpression(String s) {
        return expression == null ? s : expression;
    }

    public boolean isAny(String... any) {
        for (String s : any) {
            if (s == null) {
                if (expression == null) {
                    return true;
                }
            } else if (s.equals(expression)) {
                return true;
            }
        }
        return false;
    }

    public String getStringOrError() {
        if (expression == null) {
            throw new IllegalArgumentException("Missing value");
        }
        return expression;
    }

    public int getInt(int value) {
        try {
            return Integer.parseInt(getExpression(String.valueOf(value)));
        } catch (NumberFormatException e) {
            return value;
        }
    }

    public boolean getBoolean() {
        return getBoolean(false);
    }

    public boolean getBoolean(boolean value) {
        try {
            return Boolean.parseBoolean(getExpression(String.valueOf(value)));
        } catch (NumberFormatException e) {
            return value;
        }
    }

    public int getInt() {
        return Integer.parseInt(getStringOrError());
    }

    @Override
    public String toString() {
        return String.valueOf(expression);
    }

}
