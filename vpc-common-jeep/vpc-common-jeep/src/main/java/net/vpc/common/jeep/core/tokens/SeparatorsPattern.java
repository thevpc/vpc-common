package net.vpc.common.jeep.core.tokens;

import net.vpc.common.jeep.JTokenType;
import net.vpc.common.jeep.impl.tokens.JNamedImage;

public class SeparatorsPattern extends WordListPattern {

    public SeparatorsPattern(String name, int startId, String... words) {
        super(validateName(name), startId, JTokenPatternOrder.ORDER_OPERATOR, JTokenType.Enums.TT_GROUP_SEPARATOR, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenPatternOrder order,String... words) {
        super(validateName(name), startId, validateOrder(order), JTokenType.Enums.TT_GROUP_SEPARATOR, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenPatternOrder order, JTokenType ttype, String... words) {
        super(validateName(name), startId, order, ttype, words);
    }

    public SeparatorsPattern(String name, int startId, JTokenType ttype, String... words) {
        super(validateName(name), startId, validateOrder(null), ttype, words);
    }

//    public SeparatorsPattern(String name, int startId, JTokenType ttype, JNamedImage... words) {
//        super(validateName(name), startId, ORDER_OPERATOR, ttype, words);
//    }
//    public SeparatorsPattern(String name, int startId, int order,JTokenType ttype, JNamedImage... words) {
//        super(validateName(name), startId, validateOrder(order), ttype, words);
//    }

    public SeparatorsPattern(String name, JTokenPatternOrder order,JTokenType ttype, JNamedImage... words) {
        super(validateName(name), words[0].getPreferredId(), validateOrder(order), ttype, words);
    }

    private static String validateName(String name) {
        return name == null ? "Separators" : name;
    }

    private static JTokenPatternOrder validateOrder(JTokenPatternOrder order) {
        return order==null?JTokenPatternOrder.ORDER_OPERATOR:order;
    }
}
