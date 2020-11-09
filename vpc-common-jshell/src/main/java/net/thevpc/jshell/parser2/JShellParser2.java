package net.thevpc.jshell.parser2;

import net.thevpc.jshell.parser2.ctx.DefaultContext;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;

public class JShellParser2 {
    private StrReader strReader = new StrReader();
    private Lexer lexer = new Lexer(this);
    private Yaccer yaccer = new Yaccer(this);

    public JShellParser2(Reader reader) {
        strReader.reader = reader;
        lexer.ctx.push(new DefaultContext(this));
    }

    public static void main(String[] args) {
//        for (Node t : JShellParser2.fromString("H\\ ello  \n \"w orld $Hello\" \n `bye`").yaccer().nodes()) {
//            System.out.println(t);
//        }
        for (Yaccer.Node t : JShellParser2.fromString("#!/bin/nuts\n" +
                "ntemplate -p dir-template\n" +
                "ndocusaurus -d website build\n" +
                "cp website/nuts/build docs\n" +
                "_nuts_version=$(nuts nprops -f METADATA apiVersion)\n" +
                "cp ~/.m2/repository/net/thevpc/nuts/nuts/${_nuts_version}/nuts-${_nuts_version}.jar nuts.jar\n" +
                "\n").yaccer().nodes()) {
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

    public StrReader strReader() {
        return strReader;
    }

    public Lexer lexer() {
        return lexer;
    }

    public Yaccer yaccer() {
        return yaccer;
    }


}
