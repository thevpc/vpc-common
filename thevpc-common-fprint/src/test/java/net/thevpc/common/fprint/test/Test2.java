package net.thevpc.common.fprint.test;

import net.thevpc.common.fprint.AnsiPrintStreamSupport;
import net.thevpc.common.fprint.FPrint;
import net.thevpc.common.fprint.FormattedPrintStream;
import org.fusesource.jansi.Ansi;


import static org.fusesource.jansi.Ansi.ansi;

public class Test2 {
    public static void main(String[] args) {
        try {
            AnsiPrintStreamSupport.installStdErr(AnsiPrintStreamSupport.Type.INHERIT);

            FormattedPrintStream o = new FormattedPrintStream(System.out, FPrint.RENDERER_ANSI,FPrint.PARSER_DEFAULT);
           for (Ansi.Color c : Ansi.Color.values()) {
                String x = " " + ansi().fgBright(c) + c + ansi().reset();
                System.out.println(x);
            }
            o.println("==Hello==");
        } finally {
            AnsiPrintStreamSupport.uninstallStdOut();
        }
    }
}
