package net.vpc.common.jeep.impl.tokens;

import net.vpc.common.jeep.JToken;

import java.util.HashMap;
import java.util.Map;

public class JTokensStringFormatPlain extends AbstractJTokensStringFormat {
    public static final JTokensStringFormat INSTANCE=new JTokensStringFormatPlain();

    @Override
    public String formatDocument(Iterable<JToken> tokensb){
        return format(tokensb);
    }

    @Override
    public String format(JToken token){
        if(token==null){
            return "";
        }
        return token.image;
    }
}
