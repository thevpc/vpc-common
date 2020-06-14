package net.vpc.common.jeep.impl.tokens;

import net.vpc.common.jeep.JToken;

public interface JTokensStringFormat {
    String format(Iterable<JToken> tokensb);

    String formatDocument(Iterable<JToken> tokensb);

    String format(JToken token);
}
