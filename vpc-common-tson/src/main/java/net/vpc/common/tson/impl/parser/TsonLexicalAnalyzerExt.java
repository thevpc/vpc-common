package net.vpc.common.tson.impl.parser;

import net.vpc.common.tson.TsonLexicalAnalyzer;

public interface TsonLexicalAnalyzerExt extends TsonLexicalAnalyzer {
    String currentStreamPart();

    String currentText();

}
