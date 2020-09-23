package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.types.JAnnotationInstanceList;
import net.vpc.common.jeep.impl.types.JModifierList;

public interface JField {
    String name();

    JType type();

    Object get(Object instance);

    void set(Object instance, Object value);

    boolean isPublic();

    boolean isStatic();

    boolean isFinal();

    JType getDeclaringType();
    JAnnotationInstanceList getAnnotations() ;

    JModifierList getModifiers();

    JTypes getTypes();

}
