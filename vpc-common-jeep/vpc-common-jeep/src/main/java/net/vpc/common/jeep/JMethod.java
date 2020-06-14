package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.functions.JSignature;

public interface JMethod extends JInvokable,JDeclaration{
    String name();

    JType declaringType();

    JType[] argTypes();

    String[] argNames();

    int modifiers();
    boolean isAbstract();
    boolean isStatic();

    boolean isPublic();

    boolean isDefault();

    JTypeVariable[] typeParameters();
    JMethod parametrize(JType... parameters);
}
