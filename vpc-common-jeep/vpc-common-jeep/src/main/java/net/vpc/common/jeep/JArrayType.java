package net.vpc.common.jeep;

import net.vpc.common.jeep.JArray;
import net.vpc.common.jeep.JType;

public interface JArrayType extends JType {
    int arrayDimension();
    JType rootComponentType();
    JType componentType();
    JArray asArray(Object o);
    Object newArray(int... len);
}
