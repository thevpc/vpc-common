package net.vpc.common.jeep;

class FunctionVariable extends AbstractVariable {

    private final Function fct;

    public FunctionVariable(Function fct) {
        this.fct = fct;
    }

    @Override
    public Class getUndefinedType() {
        return Object.class;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getName() {
        return fct.getName();
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    @Override
    public Class getEffectiveType(ExpressionEvaluator evaluator) {
        return fct.getEffectiveResultType(evaluator,new ExpressionNode[0]);
    }

    @Override
    public Object getValue(ExpressionEvaluator evaluator) {
        return fct.evaluate(new ExpressionNode[0], evaluator);
    }

    @Override
    public Variable setValue(Object value, ExpressionEvaluator evaluator) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isDefinedValue() {
        return true;
    }

    @Override
    public boolean isUndefinedValue() {
        return false;
    }

    @Override
    public Variable setUndefinedValue() {
        throw new UnsupportedOperationException("Not supported");
    }
}
