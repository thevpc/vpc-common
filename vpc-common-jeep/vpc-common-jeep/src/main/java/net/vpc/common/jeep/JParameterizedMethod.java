package net.vpc.common.jeep;

import net.vpc.common.jeep.JMethod;
import net.vpc.common.jeep.JType;

public interface JParameterizedMethod extends JMethod {
    JMethod rawMethod();
    JType[] actualParameters();
}
