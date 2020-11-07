package net.thevpc.jshell.parser2;

public class Token {
    public String type;
    public Object value;

    public Token(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public boolean isWhite() {
        return "WHITE".equals(type);
    }

    public String toKeyStr(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '(': {
                    sb.append("<OPAR>");
                    break;
                }
                case ')': {
                    sb.append("<CPAR>");
                    break;
                }
                case '[': {
                    sb.append("<OSBRACK>");
                    break;
                }
                case ']': {
                    sb.append("<CSBRACK>");
                    break;
                }
                case '{': {
                    sb.append("<OCBRACK>");
                    break;
                }
                case '}': {
                    sb.append("<CCBRACK>");
                    break;
                }
                case '\"': {
                    sb.append("<DQTE>");
                    break;
                }
                case '\'': {
                    sb.append("<SQTE>");
                    break;
                }
                case '`': {
                    sb.append("<AQTE>");
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public String toStr(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\n': {
                    sb.append("\\n");
                    break;
                }
                case '\t': {
                    sb.append("\\t");
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {

        return toKeyStr(type) + "(" +
                toStr(String.valueOf(value)) +
                ')';
    }
}
