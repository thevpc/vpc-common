package net.thevpc.jshell.parser2.ctx;

import net.thevpc.jshell.parser2.AbstractContext;
import net.thevpc.jshell.parser2.StringReader2;
import net.thevpc.jshell.parser2.Token;

public class Par2Context extends AbstractContext {


    public Par2Context(StringReader2 stringReader2) {
        super(stringReader2);
    }

    @Override
    public Token nextToken() {
        StringReader2.StrReader reader = this.reader.strReader();
        int r = reader.peekChar();
        if (r < 0) {
            return null;
        }
        char rc=(char)r;
        if (r == '#') {
            return this.reader.lexer().processContext("#" ,new SharpContext(this.reader));
        }
        if (reader.readString("))")) {
            return null;
        }
        if (rc <= 32) {
            return this.reader.lexer().continueReadWhite();
        }
        if (reader.readString("$((")) {
            return this.reader.lexer().processContext("$((" ,new DollarPar2Context(this.reader));
        }
        if (reader.readString("((")) {
            return this.reader.lexer().processContext("((" ,new Par2Context(this.reader));
        }
        if (reader.readString("$(")) {
            return this.reader.lexer().processContext("$(" ,new DollarParContext(this.reader));
        }
        if (reader.readString("${")) {
            return this.reader.lexer().processContext("${" ,new DollarCurlBracketContext(this.reader));
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
            return this.reader.lexer().continueReadWord();
        }
        if (rc == '`') {
            reader.read();
            return this.reader.lexer().processContext(String.valueOf(rc),new AntiQuotedContext(this.reader));
        }
        if (rc == '\"') {
            reader.read();
            return this.reader.lexer().processContext(String.valueOf(rc),new DoubleQuotedContext(this.reader));
        }
        if (rc == '\'') {
            reader.read();
            return this.reader.lexer().processContext(String.valueOf(rc),new SimpleQuotedContext(this.reader));
        }
        reader.read();
        return new Token(String.valueOf(rc), String.valueOf(rc));
    }
}
