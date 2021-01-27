package net.thevpc.common.fprint;

import net.thevpc.common.fprint.parser.TextNode;

public interface FormattedPrintStreamParser {
    TextNode parse(String text);
    String escapeText(String text);
}
