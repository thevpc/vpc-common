package net.vpc.common.jeep;

public abstract class ReadOnlyVariable extends AbstractVariable{
    private String name;

    public ReadOnlyVariable(String name) {
        this.name = name;
    }

    @Override
    public Class getUndefinedType() {
        return getType();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public String getName() {
        return name;
    }



    @Override
    public Variable setValue(Object value, ExpressionEvaluator evaluator) {
        throw new IllegalArgumentException("Read only var "+getName());
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
        throw new IllegalArgumentException("Read only var "+getName());
    }
}
