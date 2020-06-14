package net.vpc.common.jeep;

public interface JInstanceArgumentResolver {
    JTypedValue getInstance(Object actualInstance,Object[] arguments);
}
