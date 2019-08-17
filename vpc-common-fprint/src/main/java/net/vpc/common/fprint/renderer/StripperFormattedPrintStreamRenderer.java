package net.vpc.common.fprint.renderer;

import net.vpc.common.fprint.TextFormat;
import net.vpc.common.fprint.FormattedPrintStream;
import net.vpc.common.fprint.FormattedPrintStreamRenderer;

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
