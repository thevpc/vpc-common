package net.vpc.common.jeep.core.tokens;

import net.vpc.common.jeep.JTokenType;
import static net.vpc.common.jeep.core.tokens.WordListPattern.toJTypedImages;
import net.vpc.common.jeep.impl.tokens.JNamedImage;
import net.vpc.common.jeep.impl.tokens.JTokenId;

public class OperatorsPattern extends WordListPattern {

    public OperatorsPattern(String... words) {
        this(null,words);
    }
    
    public OperatorsPattern(String name, String[] words) {
        super(JTokenId.OFFSET_OPERATORS, ORDER_OPERATOR, "Operators",
                toJTypedImages(JTokenId.OFFSET_OPERATORS, JTokenType.Enums.TT_OPERATOR, words)
        );
    }

    public OperatorsPattern(String name, JNamedImage[] words) {
        super(JTokenId.OFFSET_OPERATORS, ORDER_OPERATOR, "Operators",
                toJTypedImages(JTokenId.OFFSET_OPERATORS, JTokenType.Enums.TT_OPERATOR, words)
        );
    }

//    public OperatorsPattern(String name, int startId, String typeName, JTypedImage... words) {
//        super(startId, ORDER_OPERATOR, typeName, words);
//    }

    public OperatorsPattern(String name, int startId, JTokenType ttype, String... words) {
        super(validateName(name), startId, ttype, ORDER_OPERATOR, words);
    }

    public OperatorsPattern(String name, int startId, JTokenType ttype, int order, String... words) {
        super(validateName(name), startId, ttype, order, words);
    }

    public OperatorsPattern(String name, JTokenType ttype, JNamedImage... words) {
        super(validateName(name), words[0].getPreferredId(), ttype, ORDER_OPERATOR, words);
    }

    private static String validateName(String name) {
        return name == null ? "Operators" : name;
    }
}
