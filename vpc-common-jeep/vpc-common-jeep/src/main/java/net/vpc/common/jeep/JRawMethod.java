package net.vpc.common.jeep;

import net.vpc.common.jeep.JMethod;
import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.impl.functions.JSignature;

public interface JRawMethod extends JMethod {
    JSignature getGenericSignature();
    JType getGenericReturnType();
}
