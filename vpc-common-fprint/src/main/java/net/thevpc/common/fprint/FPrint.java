package net.thevpc.common.fprint;

import net.thevpc.common.fprint.parser.DefaultFormattedPrintStreamParser;
import net.thevpc.common.fprint.parser.FTextFallbackParser;
import net.thevpc.common.fprint.renderer.AnsiUnixTermPrintRenderer;
import net.thevpc.common.fprint.renderer.StripperFormattedPrintStreamRenderer;

public class FPrint {
    public static final FormattedPrintStreamParser PARSER_FALLBACK = FTextFallbackParser.INSTANCE;
    public static final FormattedPrintStreamParser PARSER_DEFAULT = DefaultFormattedPrintStreamParser.INSTANCE;
    public static final FormattedPrintStreamRenderer RENDERER_ANSI = AnsiUnixTermPrintRenderer.ANSI_RENDERER;
    public static final FormattedPrintStreamRenderer RENDERER_ANSI_STRIPPER = StripperFormattedPrintStreamRenderer.STRIPPER;

    public static void uninstall() {
        AnsiPrintStreamSupport.uninstall();
    }

    public static void install(AnsiPrintStreamSupport.Type type) {
        AnsiPrintStreamSupport.install(type);
    }

    public static void installStdOut(AnsiPrintStreamSupport.Type type) {
        AnsiPrintStreamSupport.installStdOut(type);
    }

    public static void uninstallStdOut() {
        AnsiPrintStreamSupport.uninstallStdOut();
    }

    public static void installStdErr(AnsiPrintStreamSupport.Type type) {
        AnsiPrintStreamSupport.installStdErr(type);
    }

    public static void uninstallStdErr() {
        AnsiPrintStreamSupport.uninstallStdErr();
    }

}
