package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.JInvoke;
import net.vpc.common.jeep.JType;

public class JFunctionLocal extends JFunctionFromInvoke {
    public JFunctionLocal(String name, JType returnType, JType[] argTypes, boolean varArgs, JInvoke handler) {
        super(name, returnType, argTypes, varArgs, handler);
    }
}
