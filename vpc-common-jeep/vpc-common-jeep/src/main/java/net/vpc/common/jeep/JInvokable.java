package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.functions.JSignature;

public interface JInvokable{
    Object invoke(JInvokeContext context);
    JSignature signature();
    JType returnType();
    default JType genericReturnType(){
        return returnType();
    }
    String name();
}
