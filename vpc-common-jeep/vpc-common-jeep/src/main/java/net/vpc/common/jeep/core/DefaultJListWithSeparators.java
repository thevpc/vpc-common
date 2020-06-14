package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JListWithSeparators;
import net.vpc.common.jeep.JToken;

import java.util.List;

public class DefaultJListWithSeparators<T> implements JListWithSeparators<T> {
    private List<T> items;
    private JToken startToken;
    private JToken endToken;
    private List<JToken> separatorTokens;

    public DefaultJListWithSeparators(List<T> items, JToken startToken, List<JToken> separatorTokens,JToken endToken) {
        this.items = items;
        this.startToken = startToken==null?null:startToken.copy();
        this.endToken = endToken==null?null:endToken.copy();
        this.separatorTokens =separatorTokens;
    }

    @Override
    public List<T> getItems() {
        return items;
    }

    @Override
    public JToken getStartToken() {
        return startToken;
    }

    @Override
    public JToken getEndToken() {
        return endToken;
    }

    @Override
    public List<JToken> getSeparatorTokens() {
        return separatorTokens;
    }
}
