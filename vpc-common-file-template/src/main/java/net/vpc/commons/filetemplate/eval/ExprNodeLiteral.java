package net.vpc.commons.filetemplate.eval;

import net.vpc.commons.filetemplate.util.StringUtils;

public class ExprNodeLiteral implements ExprNode {

    private String type;
    private Object value;

    public ExprNodeLiteral(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        switch(type){
            case "$\"":{
                return "$\"" + StringUtils.escapeString(String.valueOf(value)) + "\"";
            }
            case "\"":{
                return "\"" + StringUtils.escapeString(String.valueOf(value)) + "\"";
            }
            case "'":{
                return "'" + StringUtils.escapeString(String.valueOf(value)) + "'";
            }
            case "`":{
                return "`" + StringUtils.escapeString(String.valueOf(value)) + "`";
            }
        }
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return String.valueOf(value);
    }
}
