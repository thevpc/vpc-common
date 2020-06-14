package net.vpc.common.jeep.core.eval;

import net.vpc.common.jeep.JEvaluable;
import net.vpc.common.jeep.JInvokeContext;
import net.vpc.common.jeep.JNode;
import net.vpc.common.jeep.JType;

public class JEvaluableNode implements JEvaluable {
    private final JNode node;
    private final JType type;

    public JEvaluableNode(JNode node,JType type) {
        this.node = node;
        this.type = type;
    }

    @Override
    public JType type() {
        return type;
    }

    public JNode getNode() {
        return node;
    }

    @Override
    public Object evaluate(JInvokeContext context) {
        return context.evaluator().evaluate(node,context);
    }
}
