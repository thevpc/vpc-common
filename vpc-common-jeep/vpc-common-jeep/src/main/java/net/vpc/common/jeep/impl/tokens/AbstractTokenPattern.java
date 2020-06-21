package net.vpc.common.jeep.impl.tokens;

import net.vpc.common.jeep.JToken;
import net.vpc.common.jeep.JTokenPattern;
import net.vpc.common.jeep.JTokenizerState;
import net.vpc.common.jeep.core.tokens.JTokenDef;
import net.vpc.common.jeep.core.tokens.JTokenPatternOrder;
import net.vpc.common.jeep.util.JTokenUtils;

public abstract class AbstractTokenPattern implements JTokenPattern {

    private JTokenPatternOrder order;
    private final String name;
    private String stateName;
    private int stateId;

    public AbstractTokenPattern(JTokenPatternOrder order, String name) {
        this.order = order;
        this.name = name;
    }

    public void bindToState(JTokenizerState state) {
        this.stateId = state.getId();
        this.stateName = state.getName();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "TokenPattern[" + name + "]{"
                + "order=" + order
                + '}';
    }

    protected void fill(JTokenDef from, JToken to) {
        JTokenUtils.fillToken(from, to);
    }

    @Override
    public JTokenPatternOrder order() {
        return order;
    }

    @Override
    public String dump() {
        return toString();
    }
    
}
