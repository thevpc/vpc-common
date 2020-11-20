package net.thevpc.jshell.parser2;

import net.thevpc.jshell.parser.nodes.Node;
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

    public static void main(String[] args) {
//        for (Node t : JShellParser2.fromString("H\\ ello  \n \"w orld $Hello\" \n `bye`").yaccer().nodes()) {
//            System.out.println(t);
//        }
//        for (Yaccer.Node t : JShellParser2.fromString(
//                "#!/bin/nuts\n" +
//                "ntemplate -p dir-template\n" +
//                "ndocusaurus -d website build\n" +
//                "cp website/nuts/build docs\n" +
//                "_nuts_version=$(nuts nprops -f METADATA apiVersion)\n" +
//                "cp ~/.m2/repository/net/thevpc/nuts/nuts/${_nuts_version}/nuts-${_nuts_version}.jar nuts.jar\n" +
//                "\n").yaccer().commands()) {
//            System.out.println(t);
//        }
        for (Yaccer.Node2 t : JShellParser2.fromString(
                "a ; b | c$e ; d").yaccer().commands()) {
            System.out.println(t);
        }
//        for (Node t : JShellParser2.fromString("a=(ddd)$() fdglk").yaccer().nodes()) {
//            System.out.println(t);
//        }
//        for (Token t : JShellParser2.fromString("$() fdglk").tokens()) {
//            System.out.println(t);
//        }
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
        return yaccer().readCommand();
    }
}
