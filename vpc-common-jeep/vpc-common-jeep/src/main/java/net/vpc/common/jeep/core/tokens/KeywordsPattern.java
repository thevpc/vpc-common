package net.vpc.common.jeep.core.tokens;

import net.vpc.common.jeep.JTokenType;
import net.vpc.common.jeep.impl.tokens.JTokenId;
import net.vpc.common.jeep.impl.tokens.JTypedImage;

public class KeywordsPattern extends WordListPattern {

    public KeywordsPattern(String... words) {
        super(JTokenId.OFFSET_KEYWORDS, ORDER_KEYWORD, "Keywords",
                toJTypedImages2(JTokenId.OFFSET_KEYWORDS, JTokenType.TT_KEYWORD, "TT_KEYWORD", words)
        );
    }

    protected static JTypedImage[] toJTypedImages2(int startId, int ttype, String typeName, String[] words) {
        if (startId == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Missing PreferredId");
        } else {
            JTypedImage[] ti = new JTypedImage[words.length];
            for (int i = 0; i < ti.length; i++) {
                ti[i] = new JTypedImage(words[i], ttype, typeName, "KEYWORD_" + words[i].toUpperCase());
            }
            return ti;
        }
    }

    private static String validateName(String name) {
        return name == null ? "Keywords" : name;
    }

}
