package net.vpc.common.jeep;

public interface JParserFactory {
    JParser create(JTokenizer tokenizer, JCompilationUnit compilationUnit,JContext context);
}
