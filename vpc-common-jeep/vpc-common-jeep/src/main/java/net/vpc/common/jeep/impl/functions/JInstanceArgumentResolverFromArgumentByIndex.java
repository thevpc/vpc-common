package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.JInstanceArgumentResolver;
import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JTypedValue;
import net.vpc.common.jeep.core.DefaultJTypedValue;

public class JInstanceArgumentResolverFromArgumentByIndex implements JInstanceArgumentResolver {
    private final int instanceArgumentIndex;
    private final JType instanceArgumentType;

    public JInstanceArgumentResolverFromArgumentByIndex(int instanceArgumentIndex, JType instanceArgumentType) {
        this.instanceArgumentIndex = instanceArgumentIndex;
        this.instanceArgumentType = instanceArgumentType;
    }

    @Override
    public JTypedValue getInstance(Object actualInstance, Object[] arguments) {
        return new DefaultJTypedValue(arguments[instanceArgumentIndex], instanceArgumentType);
    }
}
