package net.vpc.common.jeep;

import net.vpc.common.jeep.nodes.ExpressionNodeConverter;

class FunctionConverter implements Function {

    private final Function oo;
    private final String name;
    private final Class[] operandTypes;
    private final int index;
    private final ExpressionEvaluatorConverter currentConverter;

    public FunctionConverter(String name, Function oo, Class[] operandTypes, int index, ExpressionEvaluatorConverter currentConverter) {
        this.oo = oo;
        this.name = name;
        this.index = index;
        this.operandTypes = FunctionsRepository.convertArgumentTypesByIndex(operandTypes, currentConverter, index);
        this.currentConverter = currentConverter;
    }

    @Override
    public Class getResultType(ExpressionManager evaluator, ExpressionNode[] args) {
        return oo.getResultType(evaluator,args);
    }

    @Override
    public Class getEffectiveResultType(ExpressionEvaluator evaluator, ExpressionNode[] args) {
        return oo.getEffectiveResultType(evaluator,args);
    }

    @Override
    public boolean isVarArgs() {
        return oo.isVarArgs();
    }

    @Override
    public Class[] getArgTypes() {
        return operandTypes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object eval(ExpressionNode[] operands, ExpressionEvaluator evaluator) {
        ExpressionNode[] operands2 = new ExpressionNode[operands.length];
        for (int j = 0; j < getArgTypes().length; j++) {
            final ExpressionNode joperand = operands[j];
            if (index == j) {
                operands2[j] = new ExpressionNodeConverter(joperand, currentConverter);
            } else {
                operands2[j] = joperand;
            }
        }
        return oo.eval(operands2, evaluator);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String n = oo.getName();
        if (n == null || n.isEmpty() || n.trim().isEmpty()) {
            n = "<IMPLICIT>";
        }
        sb.append(n).append("(");
        for (int j = 0; j < getArgTypes().length; j++) {
            if (j > 0) {
                sb.append(",");
            }
            if (index == j) {
                sb.append(currentConverter.getOriginalType().getSimpleName() + "->" + currentConverter.getTargetType().getSimpleName());
            } else {
                sb.append(getArgTypes()[j].getSimpleName());
            }
        }
        sb.append("){");
        sb.append(oo);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public MethodSignature getSignature() {
        return new MethodSignature(getName(),getArgTypes(),isVarArgs());
    }
}
