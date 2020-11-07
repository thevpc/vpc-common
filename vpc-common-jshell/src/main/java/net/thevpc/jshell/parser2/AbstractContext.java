package net.thevpc.jshell.parser2;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContext implements Context{
    final StringReader2 reader;

    public AbstractContext(StringReader2 reader) {
        this.reader = reader;
    }

    Token continueReadDollarWord(){
        int char0= reader.peekChar();
        if (char0=='$') {
            StringBuilder sb = new StringBuilder();
            reader.read();
            while (true) {
                int n = reader.read();
                if (n < 0) {
                    break;
                }
                char nc=(char)n;
                boolean doBreak = false;
                switch (n) {
                    case '\\': {
                        n = reader.read();
                        if (n >= 0) {
                            sb.append((char) n);
                        }else{
                            doBreak = true;
                        }
                        break;
                    }
                    default: {
                        if (reader.isWordChar(n)) {
                            sb.append((char) n);
                        } else {
                            reader.pushBackChar(nc);
                            doBreak = true;
                            break;
                        }
                    }
                }
                if (doBreak) {
                    break;
                }
            }
            return new Token("$WORD",sb.toString());
        }
        throw new IllegalArgumentException("unsupported");
    }
    Token continueReadWord(){
        int char0= reader.peekChar();
        if (reader.isWordChar(char0)) {
            StringBuilder sb = new StringBuilder();
            reader.read();
            sb.append((char)char0);
            while (true) {
                int n = reader.read();
                if (n < 0) {
                    break;
                }
                char nc=(char)n;
                boolean doBreak = false;
                switch (n) {
                    case '\\': {
                        n = reader.read();
                        if (n >= 0) {
                            sb.append((char) n);
                        }else{
                            doBreak = true;
                        }
                        break;
                    }
                    default: {
                        if (reader.isWordChar(n)) {
                            sb.append((char) n);
                        } else {
                            reader.pushBackChar(nc);
                            doBreak = true;
                            break;
                        }
                    }
                }
                if (doBreak) {
                    break;
                }
            }
            return new Token("WORD",sb.toString());
        }
        throw new IllegalArgumentException("unsupported");
    }


    Token processContext(String prefix,Context t){
        List<Token> all = new ArrayList<>();
        Context c = reader.ctx.peek();
        int before = reader.ctx.size();
        reader.ctx.push(t);
        while (reader.ctx.peek() != c) {
            Token tt = reader.nextToken(before);
            if (tt != null) {
                all.add(tt);
            } else {
                break;
            }
        }
        return new Token(prefix, all);
    }

    Token continueReadWhite(){
        int r= reader.peekChar();
        if (r <= 32) {
            StringBuilder sb = new StringBuilder();
            reader.read();
            sb.append((char) r);
            while (true) {
                int r2 = reader.read();
                if (r2 < 0) {
                    break;
                } else if (r2 <= 32) {
                    sb.append((char) r2);
                } else {
                    reader.pushBackChar((char) r2);
                    break;
                }
            }
            return new Token("WHITE", sb.toString());
        }
        throw new IllegalArgumentException("Unsupported");

    }
}
