package net.vpc.common.jeep.nodes;

import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionEvaluatorConverter;
import net.vpc.common.jeep.ExpressionNode;
import net.vpc.common.jeep.ExpressionEvaluator;

public class ExpressionNodeConverter extends AbstractExpressionNode {

    private final ExpressionEvaluatorConverter currentConverter;
    private final ExpressionNode node;

    public ExpressionNodeConverter(ExpressionNode node, ExpressionEvaluatorConverter currentConverter) {
        super(null);
        this.currentConverter = currentConverter;
        this.node = node;
    }

    @Override
    public Class getExprType(ExpressionManager evaluator) {
        return currentConverter.getTargetType();
    }

    @Override
    public Object evaluate(ExpressionEvaluator evaluator) {
        return currentConverter.convert(node.evaluate(evaluator));
    }

    public String toString(String prefix) {
        return prefix + currentConverter.getTargetType().getSimpleName() + "(" + node.toString() + ")";
    }

}
