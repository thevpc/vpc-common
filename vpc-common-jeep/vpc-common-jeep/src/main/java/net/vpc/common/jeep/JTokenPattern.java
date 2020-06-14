package net.vpc.common.jeep;

import net.vpc.common.jeep.core.tokens.JTokenDef;

public interface JTokenPattern {

    int ORDER_LINE_COMMENTS = 100;
    int ORDER_BLOCK_COMMENTS = 200;
    int ORDER_STRING = 300;
    int ORDER_NUMBER = 400;
    int ORDER_KEYWORD = 500;
    int ORDER_OPERATOR = 600;
    int ORDER_IDENTIFIER = 700;
    int ORDER_WHITESPACE = 1000;

    int order();

    String name();

    JTokenMatcher matcher();

    JTokenDef[] tokenDefinitions();
    
    String dump();
}
