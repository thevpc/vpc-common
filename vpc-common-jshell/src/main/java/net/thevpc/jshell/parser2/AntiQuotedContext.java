package net.thevpc.jshell.parser2;

class AntiQuotedContext extends AbstractContext {


    public AntiQuotedContext(StringReader2 stringReader2) {
        super(stringReader2);
    }

    @Override
    public Token nextToken() {
        int r = reader.peekChar();
        if (r < 0) {
            return null;
        }
        char rc=(char)r;
        if (rc == '`') {
            reader.read();
            return null;
        }
        if (rc <= 32) {
            return continueReadWhite();
        }
        if (reader.readString("$((")) {
            return processContext("$((" ,new DollarPar2Context(reader));
        }
        if (reader.readString("((")) {
            return processContext("((" ,new Par2Context(reader));
        }
        if (reader.readString("$(")) {
            return processContext("$(" ,new DollarParContext(reader));
        }
        if (reader.readString("${")) {
            return processContext("${" ,new DollarCurlBracketContext(reader));
        }
        if (rc == '$') {
            return continueReadDollarWord();
        }
        for (String s : new String[]{
                "&>>",
                "&>>",
                "&1>>",
                "&2>>",
                "&1>",
                "&2>",
                "&<",
                "<<<",
                "<<",
                ">>>",
                ">>",
                ">=",
                "<=",
                "==>",
                "==",
                "=",
                "||",
                "|&",
                "@@",
                "@",
                "&&",
                "&",
                ";",
                ",",
                ":",
                "))",
                ")",
                "{",
                "}",
                "if",
                "else",
                "while",
                "break",
                "goto",
                "label",
        }) {
            if (reader.readString(s)) {
                return new Token(s, s);
            }
        }
        if (reader.isWordChar(rc)) {
            return continueReadWord();
        }
        if (rc == '\"') {
            reader.read();
            return processContext(String.valueOf(rc),new DoubleQuotedContext(reader));
        }
        if (rc == '\'') {
            reader.read();
            return processContext(String.valueOf(rc),new SimpleQuotedContext(reader));
        }
        return new Token(String.valueOf(rc), String.valueOf(rc));
    }
}
