package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.JArgumentConverter;
import net.vpc.common.jeep.JContext;
import net.vpc.common.jeep.JType;

public class JArgumentConverterByIndex implements JArgumentConverter {
    private int newIndex;
    private JType newType;

    public JArgumentConverterByIndex(int newIndex, JType newType) {
        this.newIndex = newIndex;
        this.newType = newType;
    }

    @Override
    public JType argumentType() {
        return newType;
    }

    @Override
    public Object getArgument(int index, Object[] allArguments, JContext context) {
        return allArguments[newIndex];
    }
}