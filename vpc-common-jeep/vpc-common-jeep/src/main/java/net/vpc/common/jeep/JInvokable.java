package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.functions.JSignature;

public interface JInvokable{
    String getSourceName();
    Object invoke(JInvokeContext context);
    JSignature getSignature();
    JType getReturnType();
    default JType getGenericReturnType(){
        return getReturnType();
    }
    String getName();
}
