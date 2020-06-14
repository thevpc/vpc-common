package net.vpc.common.jeep.core.tokens;

import net.vpc.common.jeep.JTokenType;
import net.vpc.common.jeep.impl.tokens.JTokenId;

public class RegexPattern extends StringPattern{
    public static final JTokenDef REGEXP=new JTokenDef(
            JTokenId.REGEX,
            "REGEX",
            JTokenType.TT_REGEX,
            "TT_REGEX",
            "<regexp>"
    );
    public RegexPattern(String start, String end) {
        super(REGEXP, start, end);
    }
}
