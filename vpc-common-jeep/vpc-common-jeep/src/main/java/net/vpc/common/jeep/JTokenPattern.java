package net.vpc.common.jeep;

import net.vpc.common.jeep.core.tokens.JTokenDef;
import net.vpc.common.jeep.core.tokens.JTokenPatternOrder;

public interface JTokenPattern {

    JTokenPatternOrder order();

    String name();

    JTokenMatcher matcher();

    JTokenDef[] tokenDefinitions();
    
    String dump();
}
