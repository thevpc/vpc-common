package net.vpc.common.jeep;

import net.vpc.common.jeep.core.JObject;

public interface JArray extends JObject {
    JType type();

    JType componentType();

    int length();

    Object get(int index);

    void set(int index, Object value);

    Object value();
}
