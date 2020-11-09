package net.thevpc.jshell.parser2;

import java.util.*;

public class Lexer {
    private final JShellParser2 jShellParser2;
    public Stack<Context> ctx = new Stack<>();
    private LinkedList<Token> tokensBuffer = new LinkedList<>();

    public Lexer(JShellParser2 jShellParser2) {
        this.jShellParser2 = jShellParser2;
    }

    public Token continueReadDollarWord() {
        StrReader reader = jShellParser2.strReader();
        int char0 = reader.peekChar();
        if (char0 == '$') {
            StringBuilder sb = new StringBuilder();
            reader.read();
            while (true) {
                int n = reader.read();
                if (n < 0) {
                    break;
                }
                char nc = (char) n;
                boolean doBreak = false;
                switch (n) {
                    case '\\': {
                        n = reader.read();
                        if (n >= 0) {
                            sb.append((char) n);
                        } else {
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
            return new Token("$WORD", sb.toString());
        }
        throw new IllegalArgumentException("unsupported");
    }

    public Token continueReadWord() {
        StrReader reader = jShellParser2.strReader();
        int char0 = reader.peekChar();
        if (reader.isWordChar(char0)) {
            StringBuilder sb = new StringBuilder();
            reader.read();
            sb.append((char) char0);
            while (true) {
                int n = reader.read();
                if (n < 0) {
                    break;
                }
                char nc = (char) n;
                boolean doBreak = false;
                switch (n) {
                    case '\\': {
                        n = reader.read();
                        if (n >= 0) {
                            sb.append((char) n);
                        } else {
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
            return new Token("WORD", sb.toString());
        }
        throw new IllegalArgumentException("unsupported");
    }


    public Token continueReadWhite() {
        StrReader reader = jShellParser2.strReader();
        int r = reader.peekChar();
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

    public Token processContext(String prefix, Context t) {
        StrReader reader = jShellParser2.strReader();
        List<Token> all = new ArrayList<>();
        Context c = ctx.peek();
        int before = ctx.size();
        ctx.push(t);
        while (jShellParser2.lexer().ctx.peek() != c) {
            Token tt = jShellParser2.lexer().nextToken(before);
            if (tt != null) {
                all.add(tt);
            } else {
                break;
            }
        }
        return new Token(prefix, all);
    }

    public void popContext() {
        ctx.pop();
    }

    public Iterable<Token> tokens() {
        return new Iterable<Token>() {
            @Override
            public Iterator<Token> iterator() {
                return new Iterator<Token>() {
                    Token t;

                    @Override
                    public boolean hasNext() {
                        t = nextToken();
                        return t != null;
                    }

                    @Override
                    public Token next() {
                        return t;
                    }
                };
            }
        };
    }

    public void pushBackToken(Token t) {
        if (t != null) {
            tokensBuffer.addFirst(t);
        }
    }


    public void skipWhites() {
        while (true) {
            Token t = peedToken();
            if (t == null) {
                return;
            }
            if (!t.type.equals("WHITE")) {
                return;
            }
            nextToken();
        }
    }

    public Token nextNonWhiteToken() {
        while (true) {
            Token t = nextToken();
            if (t == null) {
                return null;
            }
            if (!t.type.equals("WHITE")) {
                return t;
            }
        }
    }

    public Token peedToken() {
        if (!tokensBuffer.isEmpty()) {
            return tokensBuffer.getFirst();
        } else {
            while (true) {
                if (ctx.isEmpty()) {
                    return null;
                }
                Token u = ctx.peek().nextToken();
                if (u != null) {
                    tokensBuffer.add(u);
                    return u;
                }
                ctx.pop();
            }
        }
    }

    public Token nextToken() {
        if (!tokensBuffer.isEmpty()) {
            return tokensBuffer.removeFirst();
        } else {
            while (true) {
                if (ctx.isEmpty()) {
                    return null;
                }
                Token u = ctx.peek().nextToken();
                if (u != null) {
                    return u;
                }
                ctx.pop();
            }
        }
    }

    public Token nextToken(int before) {
        while (true) {
            if (ctx.isEmpty()) {
                return null;
            }
            Token u = ctx.peek().nextToken();
            if (u != null) {
                return u;
            }
            ctx.pop();
            if (ctx.size() <= before) {
                return null;
            }
        }
    }
}
