package net.vpc.common.jeep;

public interface JArgumentConverter {
    JType argumentType();
    Object getArgument(int index,Object[] allArguments, JContext context);
}
