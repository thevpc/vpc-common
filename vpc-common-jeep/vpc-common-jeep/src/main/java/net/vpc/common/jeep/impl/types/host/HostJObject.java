package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.core.JObject;
import net.vpc.common.jeep.util.JeepPlatformUtils;

import java.lang.reflect.Field;

public class HostJObject implements JObject {
    private JType type;
    private Object object;
    private Class clazz;

    public HostJObject(JType type, Object object,Class clazz) {
        this.type = type;
        this.object = object;
        this.clazz = clazz;
    }

    @Override
    public JType type() {
        return type;
    }

    @Override
    public Object get(String name) {
        try {
            Field field = ((Class) clazz).getField(name);
            JeepPlatformUtils.setAccessibleWorkaround(field);
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Field not found "+name);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found "+name);
        }
    }

    @Override
    public void set(String name, Object o) {
        try {
            Field field = ((Class) clazz).getField(name);
            JeepPlatformUtils.setAccessibleWorkaround(field);
            field.set(null,o);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Field not found "+name);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found "+name);
        }
    }
}
