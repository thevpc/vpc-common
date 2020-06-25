package net.vpc.common.jeep;

import net.vpc.common.jeep.JConstructor;
import net.vpc.common.jeep.JMethod;
import net.vpc.common.jeep.JType;

public interface JParameterizedConstructor extends JConstructor {
    JConstructor getRawConstructor();
    JType[] getActualParameters();
}
