package net.vpc.common.jeep;

public interface JTokenFactory {
    JTokenizer create(JTokenizerReader reader, JTokenConfig config, boolean skipComments, boolean skipSpaces, JContext context);
}
