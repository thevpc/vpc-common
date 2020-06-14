package net.vpc.common.jeep.core.tokens;

import net.vpc.common.jeep.JTokenType;
import net.vpc.common.jeep.impl.tokens.JTokenId;

public class TemporalPattern extends StringPattern{
    public static final JTokenDef TEMPORAL=new JTokenDef(
            JTokenId.TEMPORAL,
            "TEMPORAL",
            JTokenType.TT_TEMPORAL,
            "TT_TEMPORAL",
            "t\"yyyy-MM-dd\""
    );
    public TemporalPattern(String start, String end) {
        super(TEMPORAL, start, end);
    }
    public TemporalPattern(JTokenDef info, String start, String end) {
        super(info, start, end);
    }
}
