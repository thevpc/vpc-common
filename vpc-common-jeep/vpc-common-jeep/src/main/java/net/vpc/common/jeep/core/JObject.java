package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JType;

public interface JObject {
    JType type();
    Object get(String name);
    void set(String name,Object o);
}
