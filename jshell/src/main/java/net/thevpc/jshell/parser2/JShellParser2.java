package net.thevpc.jshell.parser2;

import net.thevpc.jshell.parser2.ctx.DefaultContext;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class JShellParser2 {
    private StrReader strReader = new StrReader();
    private DefaultLexer lexer = new DefaultLexer(this);
    private Yaccer yaccer = new Yaccer(this.lexer);

    public JShellParser2(Reader reader) {
        strReader.reader = reader;
        lexer.ctx.push(new DefaultContext(this));
    }

    public static JShellParser2 fromString(String s) {
        return new JShellParser2(new StringReader(s == null ? "" : s));
    }

    public static JShellParser2 fromInputStream(InputStream s) {
        return new JShellParser2(s==null?new StringReader("") : new InputStreamReader(s));
    }

    public StrReader strReader() {
        return strReader;
    }

    public DefaultLexer lexer() {
        return lexer;
    }

    public Yaccer yaccer() {
        return yaccer;
    }


    public Node parse() {
        return yaccer().readScript();
    }
}
