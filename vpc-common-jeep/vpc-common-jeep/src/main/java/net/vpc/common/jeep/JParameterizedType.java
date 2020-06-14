package net.vpc.common.jeep;

import net.vpc.common.jeep.JType;

public interface JParameterizedType extends JType {
    JType[] actualTypeArguments();
}
