package net.vpc.common.fprint;

public interface FormattedPrintStreamRenderer {
    void startFormat(FormattedPrintStream out, TextFormat format);
    void endFormat(FormattedPrintStream out, TextFormat color);
}
