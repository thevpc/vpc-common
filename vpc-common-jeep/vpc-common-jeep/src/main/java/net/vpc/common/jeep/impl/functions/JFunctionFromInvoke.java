package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JFunctionBase;

public class JFunctionFromInvoke extends JFunctionBase {
    private JInvoke handler;

    public JFunctionFromInvoke(String name, JType returnType, JType[] argTypes, boolean varArgs, JInvoke handler) {
        super(name, returnType, argTypes,varArgs);
        this.handler = handler;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        return handler.invoke(icontext);
    }

    @Override
    public String getSourceName() {
        return "<unknown-source>";
    }
}
