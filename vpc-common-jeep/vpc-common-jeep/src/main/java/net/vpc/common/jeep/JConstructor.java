package net.vpc.common.jeep;

public interface JConstructor extends JInvokable, JDeclaration {
    JType[] argTypes();

    String[] argNames();

    JTypeVariable[] typeParameters();

    JType declaringType();

    boolean isPublic();
    int modifiers();
}
