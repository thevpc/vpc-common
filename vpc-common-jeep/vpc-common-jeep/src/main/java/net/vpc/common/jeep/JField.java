package net.vpc.common.jeep;

public interface JField {
    String name();

    JType type();

    Object get(Object instance);

    void set(Object instance, Object value);

    int modifiers();

    boolean isPublic();

    boolean isStatic();

    boolean isFinal();

    JType declaringType();
}
