package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.JAnnotationInstance;

import java.util.List;
import java.util.stream.Stream;

public interface JAnnotationInstanceList {
    Stream<JAnnotationInstance> stream();
    JAnnotationInstance[] toArray();
    <T> T[] toArray(Class<T> cls);
    List<JAnnotationInstance> toList();
    boolean contains(JAnnotationInstance a);
}
