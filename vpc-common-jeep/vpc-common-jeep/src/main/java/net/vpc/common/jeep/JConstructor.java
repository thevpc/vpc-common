package net.vpc.common.jeep;

public interface JConstructor extends JInvokable, JDeclaration {
    JType[] getArgTypes();

    String[] getArgNames();

    JTypeVariable[] getTypeParameters();

    JType getDeclaringType();

    boolean isPublic();
    int getModifiers();
}
