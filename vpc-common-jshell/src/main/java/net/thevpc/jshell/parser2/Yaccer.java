package net.thevpc.jshell.parser2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Yaccer {

    private final JShellParser2 jShellParser2;

    public Yaccer(JShellParser2 jShellParser2) {
        this.jShellParser2 = jShellParser2;
    }

    public Iterable<Node> nodes() {
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {
                    Node n = null;

                    @Override
                    public boolean hasNext() {
                        n = readNode();
                        return n != null;
                    }

                    @Override
                    public Node next() {
                        return n;
                    }
                };
            }
        };
    }

    public Node readNode() {
        jShellParser2.lexer().skipWhites();
        Token u = jShellParser2.lexer().peedToken();
        if (u == null) {
            return null;
        }
        if (u.type.equals("#")) {
            return readComments();
        }
        if (u.type.equals("(")) {
            return readPar();
        }
        return readLine();
    }

    public Par readPar() {
        Token u = jShellParser2.lexer().peedToken();
        if (u == null) {
            return null;
        }
        if (u.type.equals("(")) {
            jShellParser2.lexer().nextToken();
            Node n = readNode();
            u = jShellParser2.lexer().peedToken();
            if (u == null) {
                return null;
            }
            return readPar();
        }
        return null;
    }

    public ArgumentsLine readLine() {
        Argument a = readFirstArgument();
        if (a == null) {
            return null;
        }
        List<Argument> all = new ArrayList<>();
        all.add(a);
        while (true) {
            Token y = jShellParser2.lexer().peedToken();
            if (y == null) {
                break;
            }
            if (y.isWhite()) {
                jShellParser2.lexer().nextToken();
                if (y.isNewline()) {
                    break;
                }
                Argument r = readArgument();
                if (r != null) {
                    all.add(r);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return new ArgumentsLine(all);
    }

    public Argument readFirstArgument() {
        return readArgument();
    }

    public Comments readComments() {
        List<Token> ok = new ArrayList<>();
        while (true) {
            Token t = jShellParser2.lexer().peedToken();
            if (t == null) {
                break;
            }
            if (t.type.equals("#")) {
                jShellParser2.lexer().nextToken();
                ok.add(t);
            } else {
                break;
            }
        }
        if (ok.isEmpty()) {
            return null;
        }
        return new Comments(ok);
    }

    public Argument readArgument() {
        List<Token> ok = new ArrayList<>();
        boolean loop = true;
        while (loop) {
            Token t = jShellParser2.lexer().peedToken();
            if (t == null) {
                break;
            }
            switch (t.type) {
                case "WORD":
                case "$WORD":
                case "$(":
                case "$((":
                case "${":
                case "(":
                case "{":
                case "=":
                case ":":
                case "\"":
                case "'":
                case "`": {
                    jShellParser2.lexer().nextToken();
                    ok.add(t);
                    break;
                }
                default: {
                    loop = false;
                    break;
                }
            }
        }
        if (ok.isEmpty()) {
            return null;
        }
        return new Argument(ok);
    }



    public interface Node {

    }

    public class ArgumentsLine implements Node {
        List<Argument> args;

        public ArgumentsLine(List<Argument> args) {
            this.args = args;
        }

        @Override
        public String toString() {
            return "ArgumentsLine{" +
                    args +
                    '}';
        }
    }

    public class Par implements Node {
        Node element;

        public Par(Node element) {
            this.element = element;
        }
    }

    public class Argument implements Node {
        List<Token> tokens;

        public Argument(List<Token> tokens) {
            this.tokens = tokens;
        }

        @Override
        public String toString() {
            return "Argument{" +
                    tokens +
                    '}';
        }
    }

    public class Comments implements Node {
        List<Token> tokens;

        public Comments(List<Token> tokens) {
            this.tokens = tokens;
        }

        @Override
        public String toString() {
            return "Comments{" +
                    tokens +
                    '}';
        }
    }
}
