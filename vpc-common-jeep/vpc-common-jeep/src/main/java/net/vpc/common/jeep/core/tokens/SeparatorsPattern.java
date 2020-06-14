package net.vpc.common.jeep.core.tokens;

import static net.vpc.common.jeep.JTokenPattern.ORDER_OPERATOR;
import net.vpc.common.jeep.JTokenType;
import net.vpc.common.jeep.impl.tokens.JNamedImage;

public class SeparatorsPattern extends WordListPattern {

    public SeparatorsPattern(String name, int startId, String... words) {
        super(validateName(name), startId, JTokenType.Enums.TT_GROUP_SEPARATOR, ORDER_OPERATOR, words);
    }

    public SeparatorsPattern(String name, int startId, int order, JTokenType ttype, String... words) {
        super(validateName(name), startId, ttype, order, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenType ttype, String... words) {
        super(validateName(name), startId, ttype, ORDER_OPERATOR, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenType ttype, JNamedImage... words) {
        super(validateName(name), startId, ttype, ORDER_OPERATOR, words);
    }

    public SeparatorsPattern(String name, JTokenType ttype, JNamedImage... words) {
        super(validateName(name), words[0].getPreferredId(), ttype, ORDER_OPERATOR, words);
    }

    private static String validateName(String name) {
        return name == null ? "Separators" : name;
    }
}
