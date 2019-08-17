package net.vpc.common.jeep;

import java.lang.reflect.Field;

class FieldVariable extends AbstractVariable {

    private final Field field;
    private final Object instance;
    private final boolean readOnly;

    public FieldVariable(Object instance, Field field, boolean readOnly) {
        this.field = field;
        this.field.setAccessible(true);
        this.instance = instance;
        this.readOnly = readOnly;
    }

    @Override
    public Class getUndefinedType() {
        return field.getType();
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public Class getType() {
        return field.getType();
    }

    @Override
    public Object getValue(ExpressionEvaluator evaluator) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public Variable setValue(Object value, ExpressionEvaluator evaluator) {
        if (readOnly) {
            throw new UnsupportedOperationException("Not supported");
        }
        try {
            field.set(instance, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
        return this;
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
