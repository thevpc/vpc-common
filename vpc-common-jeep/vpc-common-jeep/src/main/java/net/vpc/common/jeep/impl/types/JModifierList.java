package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.JModifier;

import java.util.List;

public interface JModifierList {
    JModifier[] toArray();
    <T> T[] toArray(Class<T> cls);
    List<JModifier> toList();
    boolean contains(JModifier a);
}
