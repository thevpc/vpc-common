package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.types.AbstractJField;
import net.vpc.common.jeep.util.JTypeUtils;

public class JParameterizedFieldImpl extends AbstractJField implements JParameterizedField {
    private JField rawField;
    private JParameterizedType declaringType;
    private JType fieldType;
    public JParameterizedFieldImpl(JRawField rawField, JParameterizedType declaringType) {
        this.rawField=rawField;
        this.declaringType=declaringType;
        this.fieldType= JTypeUtils.buildActualType(rawField.genericType(),declaringType);
    }

    @Override
    public String name() {
        return rawField.name();
    }

    @Override
    public JType type() {
        return fieldType;
    }

    @Override
    public Object get(Object instance) {
        return rawField.get(instance);
    }

    @Override
    public void set(Object instance, Object value) {
        rawField.set(instance,value);
    }

    @Override
    public boolean isPublic() {
        return rawField.isPublic();
    }

    @Override
    public boolean isStatic() {
        return rawField.isStatic();
    }

    @Override
    public boolean isFinal() {
        return rawField.isFinal();
    }

    @Override
    public int modifiers() {
        return rawField.modifiers();
    }

    @Override
    public JType declaringType() {
        return declaringType;
    }
}