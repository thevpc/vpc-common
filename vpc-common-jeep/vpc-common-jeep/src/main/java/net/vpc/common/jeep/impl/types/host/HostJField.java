package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JTypes;
import net.vpc.common.jeep.core.types.AbstractJField;
import net.vpc.common.jeep.JRawField;
import net.vpc.common.jeep.impl.types.JTypesHostHelper;
import net.vpc.common.jeep.util.JTypeUtils;
import net.vpc.common.jeep.util.JeepPlatformUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class HostJField extends AbstractJField implements JRawField {
    private JType declaringType;
    private Field field;
    private JType fieldType;
    private JType genericFieldType;

    public HostJField(Field field, JType actualType, JType declaringType) {
        this.declaringType = declaringType;
        this.field = field;
        this.genericFieldType = htypes().forName(field.getGenericType(), declaringType);
        this.fieldType = JTypeUtils.buildRawType(this.genericFieldType, declaringType);
        JeepPlatformUtils.setAccessibleWorkaround(field);
    }

    protected JTypesHostHelper htypes(){
        return new JTypesHostHelper(types());
    }

    protected JTypes types(){
        return declaringType.types();
    }

    public String name() {
        return field.getName();
    }

    @Override
    public JType type() {
        return fieldType;
    }

    @Override
    public JType genericType() {
        return genericFieldType;
    }

    @Override
    public Object get(Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void set(Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    @Override
    public int modifiers() {
        return field.getModifiers();
    }

    @Override
    public JType declaringType() {
        return declaringType;
    }
}
