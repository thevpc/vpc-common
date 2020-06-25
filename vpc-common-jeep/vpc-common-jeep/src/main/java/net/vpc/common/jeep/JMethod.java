package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.functions.JSignature;

public interface JMethod extends JInvokable,JDeclaration{
    String getName();

    JType getDeclaringType();

    JType[] getArgTypes();

    String[] getArgNames();

    int getModifiers();
    boolean isAbstract();
    boolean isStatic();

    boolean isPublic();

    boolean isDefault();

    JTypeVariable[] getTypeParameters();
    JMethod parametrize(JType... parameters);
}
