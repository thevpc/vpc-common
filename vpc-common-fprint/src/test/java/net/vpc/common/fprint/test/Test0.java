package net.vpc.common.fprint.test;

import net.vpc.common.fprint.AnsiPrintStreamSupport;
import net.vpc.common.fprint.FPrint;
import net.vpc.common.fprint.FormattedPrintStream;


public class Test0 {
    public static void main(String[] args) {
//        AnsiMain.main(args);
        try {
            AnsiPrintStreamSupport.installStdErr(AnsiPrintStreamSupport.Type.INHERIT);

            FormattedPrintStream o = new FormattedPrintStream(System.out, FPrint.RENDERER_ANSI,FPrint.PARSER_DEFAULT);
//            o.println("Hello **Wol**rd");
//            o.println("@@Text@@");
            o.println("[[]]");
//            o.println("\\@Text\\@           | @@Text@@\n");
//            o.println("@@Text@@         | @@Text@@\n");
//            o.println("\\@\\@Text\\@\\@         | @@Text@@\n");
//            o.println("\\_\\_Text\\_\\_");
//            o.println("[[Text]]");
//            o.println("\\\"\\\"Text\\\"\\\"         | \"\"Text\"\"");
//            o.println(":::: \\\"Text\\\"           | \"Text\"");
//            o.println("__Text__");
//            o.flush();
//            o.println("==Hello==");
        } finally {
            AnsiPrintStreamSupport.uninstallStdOut();
        }
    }
}
