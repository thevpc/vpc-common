package net.vpc.common.jeep;

import java.util.ArrayList;
import java.util.List;

public class JTokenBoundsValue implements JTokenBounds{
    private JToken startToken;
    private JToken endToken;
    private JToken[] separators;

    public JTokenBoundsValue(JToken startToken, JToken endToken, JToken[] separators) {
        this.startToken = startToken;
        this.endToken = endToken;
        this.separators = separators;
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
    public JToken[] getSeparators() {
        return separators;
    }
}
