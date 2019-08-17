package net.vpc.common.fprint;

import net.vpc.common.fprint.parser.TextNode;

public interface FormattedPrintStreamParser {
    TextNode parse(String text);
    String escapeText(String text);
}
