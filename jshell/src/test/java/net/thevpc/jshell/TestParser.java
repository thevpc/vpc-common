package net.thevpc.jshell;

import net.thevpc.jshell.parser2.JShellParser2;
import net.thevpc.jshell.parser2.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestParser {
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
//        for (Node t : JShellParser2.fromString("a=(ddd)$() fdglk").yaccer().nodes()) {
//            System.out.println(t);
//        }
//        for (Token t : JShellParser2.fromString("$() fdglk").tokens()) {
//            System.out.println(t);
//        }
    }
    @Test
    public void test1(){
        Node node = JShellParser2.fromString("a='aa' ; b=\"bb\" ;c=`cc$(ls)` ;").parse();
        Assertions.assertEquals(
                "ArgumentsLine{[[WORD(a), =, <SQTE>([<SQTE>(aa)])]]} ; ArgumentsLine{[[WORD(b), =, <DQTE>([STR(bb)])]]} ; ArgumentsLine{[[WORD(c), =, <AQTE>([WORD(cc), $<OPAR>([WORD(ls)])])]]} ;",
                String.valueOf(node)
        );
        System.out.println(node);
    }
    @Test
    public void test2(){
        Node node = JShellParser2.fromString("b=\"dd\" aa;").parse();
        Assertions.assertEquals(
                "ArgumentsLine{[[WORD(b), =, <DQTE>([STR(dd)])], WORD(aa)]} ;",
                String.valueOf(node)
        );
        System.out.println(node);
    }
}
