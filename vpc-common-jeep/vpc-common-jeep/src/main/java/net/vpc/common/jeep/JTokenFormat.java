package net.vpc.common.jeep;

import net.vpc.common.jeep.core.tokens.DefaultJTokenFormat;
import net.vpc.common.jeep.core.tokens.JTokenColumnFormat;
import net.vpc.common.jeep.core.tokens.JTokenDef;

public interface JTokenFormat {
    JTokenFormat COLUMNS=new JTokenColumnFormat();
    JTokenFormat DEFAULT=new DefaultJTokenFormat();
    String format(JToken token) ;
    String format(JTokenDef token) ;
}
