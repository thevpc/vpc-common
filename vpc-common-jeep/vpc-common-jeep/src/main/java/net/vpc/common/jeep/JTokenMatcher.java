package net.vpc.common.jeep;

import net.vpc.common.jeep.core.tokens.JTokenPatternOrder;

public interface JTokenMatcher {

    JTokenPatternOrder order();

    JTokenMatcher reset();

    boolean matches(char c);

    boolean matches(String c);

    String image();

    Object value();

    boolean matches(JTokenizerReader reader, JToken outputToken);

    /**
     * return
     *   -1 if not valid and should rollback all read chars
     *   0 if valid and should accept all read chars
     *   n>0 if valid and should rollback 'n' read chars
     * @return valid status
     */
    boolean valid();

    JTokenPattern pattern();
}
