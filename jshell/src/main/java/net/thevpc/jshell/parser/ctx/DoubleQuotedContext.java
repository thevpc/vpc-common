package net.thevpc.jshell.parser.ctx;

import net.thevpc.jshell.parser.AbstractContext;
import net.thevpc.jshell.parser.JShellParser;
import net.thevpc.jshell.parser.StrReader;
import net.thevpc.jshell.parser.Token;

public class DoubleQuotedContext extends AbstractContext {
    public DoubleQuotedContext(JShellParser jshp) {
        super(jshp);
    }

    @Override
    public Token nextToken() {
        StrReader reader = this.reader.strReader();
        int r = reader.peekChar();
        if (r < 0) {
            return null;
        }
        char rc = (char) r;

        if (reader.readString("$((")) {
            return this.reader.lexer().processContext("$((", new DollarPar2Context(this.reader));
        }
        if (reader.readString("$(")) {
            return this.reader.lexer().processContext("$(", new DollarParContext(this.reader));
        }
        if (reader.readString("${")) {
            return this.reader.lexer().processContext("${", new DollarCurlBracketContext(this.reader));
        }
        if (rc == '$') {
            return this.reader.lexer().continueReadDollarWord();
        }
        if (rc == '\"') {
            reader.read();
            return null;
        }
        if (rc == '`') {
            reader.read();
            return this.reader.lexer().processContext(String.valueOf(rc), new AntiQuotedContext(this.reader));
        }


        StringBuilder sb = new StringBuilder();
        while (true) {
            r = reader.peekChar();
            if (r >= 0) {
                rc = (char) r;
                if (isDblQChar(rc)) {
                    sb.append((char) reader.read());
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        if(sb.length()==0){
            return null;
        }
        return new Token("STR", sb.toString());
    }

    boolean isDblQChar(char c) {
        switch (c) {
            case '\'':
            case '\"':
            case '$': {
                return false;
            }
        }
        return true;
    }
}
