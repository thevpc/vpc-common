package net.thevpc.jshell.parser2;

import net.thevpc.jshell.parser2.ctx.DefaultContext;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.*;

public class StringReader2 {
    private StrReader strReader = new StrReader();
    private Lexer lexer = new Lexer();
    private Yaccer yaccer = new Yaccer();

    public StringReader2(Reader reader) {
        strReader.reader = reader;
        lexer.ctx.push(new DefaultContext(this));
    }

    public static void main(String[] args) {
//        for (Node t : StringReader2.fromString("H\\ ello  \n \"w orld $Hello\" \n `bye`").yaccer().nodes()) {
//            System.out.println(t);
//        }
        for (Node t : StringReader2.fromString("#!/bin/nuts\n" +
                "ntemplate -p dir-template\n" +
                "ndocusaurus -d website build\n" +
                "cp website/nuts/build docs\n" +
                "_nuts_version=$(nuts nprops -f METADATA apiVersion)\n" +
                "cp ~/.m2/repository/net/vpc/theapp/nuts/nuts/${_nuts_version}/nuts-${_nuts_version}.jar nuts.jar\n" +
                "\n").yaccer().nodes()) {
            System.out.println(t);
        }
//        for (Node t : StringReader2.fromString("a=(ddd)$() fdglk").yaccer().nodes()) {
//            System.out.println(t);
//        }
//        for (Token t : StringReader2.fromString("$() fdglk").tokens()) {
//            System.out.println(t);
//        }
    }

    public static StringReader2 fromString(String s) {
        return new StringReader2(new StringReader(s == null ? "" : s));
    }

    public StrReader strReader() {
        return strReader;
    }

    public Lexer lexer() {
        return lexer;
    }

    public Yaccer yaccer() {
        return yaccer;
    }

    public interface Node {

    }

    public class StrReader {
        private Reader reader;
        private boolean uniformNewLine = true;
        private StringBuilder buffer = new StringBuilder();

        public void pushBackChar(char c) {
            buffer.insert(0, c);
        }

//    public Reader nextItem() {
//
//    }

        public int getChar(int pos) {
            if (pos >= buffer.length()) {
                while (true) {
                    if (fillLine() <= 0) {
                        break;
                    }
                    if (pos < buffer.length()) {
                        break;
                    }
                }
            }
            if (pos >= buffer.length()) {
                return -1;
            }
            return buffer.charAt(pos);
        }

        public boolean readString(String str) {
            if (isString(str, 0)) {
                readAll(str.length());
                return true;
            }
            return false;
        }

        public boolean isString(String str, int pos) {
            for (int i = 0; i < str.length(); i++) {
                if (!isChar(str.charAt(i), pos + i)) {
                    return false;
                }
            }
            return true;
        }


        public boolean isChar(char c, int pos) {
            return getChar(pos) == c;
        }


        public boolean isWordChar(int c) {
            if (c == -1) {
                return false;
            }
            switch (c) {
                case '(':
                case ')':
                case '{':
                case '}':
                case '\\':
                case '$':
                case '"':
                case '\'':
                case '`':
                case '<':
                case '>':
                case '|':
                case '&':
                case '=':
                case ':':
                case '+':
                case '*':
                case '?':
                case '[':
                case ']':
                case '#': {
                    return false;
                }
                default: {
                    if (c <= 32) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Reader toReader() {
            return new Reader() {
                @Override
                public int read(char[] cbuf, int off, int len) throws IOException {
                    return StrReader.this.read(cbuf, off, len);
                }

                @Override
                public void close() throws IOException {
                    StrReader.this.close();
                }
            };
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            int x = 0;
            for (int i = 0; i < len; i++) {
                int c = read();
                if (c == -1) {
                    return i;
                }
                cbuf[off + i] = (char) c;
            }
            return len;
        }

        public Iterable<String> lines() {
            return new Iterable<String>() {
                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {
                        String line;

                        @Override
                        public boolean hasNext() {
                            line = readLine();
                            return line != null;
                        }

                        @Override
                        public String next() {
                            return line;
                        }
                    };
                }
            };
        }

        public String readLine() {
            StringBuilder sb = new StringBuilder();
            while (true) {
                int r = read();
                if (r < 0) {
                    break;
                }
                sb.append((char) r);
            }
            if (sb.length() == 0) {
                return null;
            }
            return sb.toString();
        }

        public int peekChar() {
            if (buffer.length() == 0) {
                fillLine();
            }
            if (buffer.length() > 0) {
                return buffer.charAt(0);
            }
            return -1;
        }

        public int read() {
            if (buffer.length() == 0) {
                fillLine();
            }
            if (buffer.length() > 0) {
                char c = buffer.charAt(0);
                buffer.deleteCharAt(0);
                return c;
            }
            return -1;
        }

        private int read0() {
            try {
                return reader.read();
            } catch (IOException e) {
                return -1;
            }
        }


        private int fillLine() {
            int count = 0;
            while (true) {
                int e = read0();
                if (e == -1) {
                    break;
                }
                count++;
                if (e == '\\') {
                    int e2 = read0();
                    if (e2 == -1) {
                        buffer.append('\\');
                        break;
                    } else if (e2 == '\n') {
                        count++;
                        break; //new line
                    } else if (e2 == '\r') {
                        count++;
                        e2 = read0();
                        if (e2 == '\n') {
                            count++;
                            break; //new line
                        } else if (e2 == -1) {
                            break; //end of file
                        } else {
                            count++;
                            buffer.append((char) e2);
                            break; //new line
                        }
                    } else {
                        count++;
                        buffer.append('\\');
                        buffer.append((char) e2);
                    }
                } else if (e == '\r') {
                    int e2 = read0();
                    if (e2 == -1) {
                        if (uniformNewLine) {
                            buffer.append('\n');
                        } else {
                            buffer.append("\r");
                        }
                        break; //new line
                    } else if (e2 == '\n') {
                        count++;
                        if (uniformNewLine) {
                            buffer.append('\n');
                        } else {
                            buffer.append("\r\n");
                        }
                        break; //new line
                    } else {
                        count++;
                        buffer.append((char) e2);
                    }
                } else {
                    count++;
                    buffer.append((char) e);
                }
            }
            return count;
        }

        public void close() {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        public String readAll(int length) {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int r = read();
                if (r < 0) {
                    throw new IllegalArgumentException("Invalid");
                }
                sb.append((char) r);
            }
            return sb.toString();
        }
    }

    public class Lexer {
        public Stack<Context> ctx = new Stack<>();
        private LinkedList<Token> tokensBuffer = new LinkedList<>();

        public Token continueReadDollarWord() {
            StringReader2.StrReader reader = strReader();
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
            StringReader2.StrReader reader = strReader();
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
            StringReader2.StrReader reader = strReader();
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
            StringReader2.StrReader reader = strReader();
            List<Token> all = new ArrayList<>();
            Context c = ctx.peek();
            int before = ctx.size();
            ctx.push(t);
            while (lexer().ctx.peek() != c) {
                Token tt = lexer().nextToken(before);
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

    public class Yaccer {

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
            lexer().skipWhites();
            Token u = lexer().peedToken();
            if(u==null){
                return null;
            }
            if(u.type.equals("#")){
                return readComments();
            }
            return readLine();
        }

        public ArgumentsLine readLine() {
            Argument a = readFirstArgument();
            if (a == null) {
                return null;
            }
            List<Argument> all = new ArrayList<>();
            all.add(a);
            while (true) {
                Token y = lexer().peedToken();
                if (y == null) {
                    break;
                }
                if (y.isWhite()) {
                    lexer().nextToken();
                    if(y.isNewline()){
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
            while (true){
                Token t = lexer().peedToken();
                if(t==null){
                    break;
                }
                if(t.type.equals("#")){
                    lexer().nextToken();
                    ok.add(t);
                }else{
                    break;
                }
            }
            if(ok.isEmpty()){
                return null;
            }
            return new Comments(ok);
        }

        public Argument readArgument() {
            List<Token> ok = new ArrayList<>();
            boolean loop=true;
            while (loop){
                Token t = lexer().peedToken();
                if(t==null){
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
                    case "`":
                    {
                        lexer().nextToken();
                        ok.add(t);
                        break;
                    }
                    default: {
                        loop=false;
                        break;
                    }
                }
            }
            if(ok.isEmpty()){
                return null;
            }
            return new Argument(ok);
        }
    }
}
