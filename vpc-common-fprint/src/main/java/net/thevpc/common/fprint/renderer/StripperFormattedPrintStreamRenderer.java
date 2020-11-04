package net.thevpc.common.fprint.renderer;

import net.thevpc.common.fprint.TextFormat;
import net.thevpc.common.fprint.FormattedPrintStream;
import net.thevpc.common.fprint.FormattedPrintStreamRenderer;

public class StripperFormattedPrintStreamRenderer implements FormattedPrintStreamRenderer {
    public static final FormattedPrintStreamRenderer STRIPPER=new StripperFormattedPrintStreamRenderer();
    @Override
    public void startFormat(FormattedPrintStream out, TextFormat format) {
       //do nothing
    }

    @Override
    public void endFormat(FormattedPrintStream out, TextFormat color) {
        //
    }
}
