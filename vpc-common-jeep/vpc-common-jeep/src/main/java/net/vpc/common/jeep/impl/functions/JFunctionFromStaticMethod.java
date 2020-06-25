package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.JFunction;
import net.vpc.common.jeep.JInvokeContext;
import net.vpc.common.jeep.JMethod;
import net.vpc.common.jeep.JType;

public class JFunctionFromStaticMethod implements JFunction {
    private JMethod method;
    public JFunctionFromStaticMethod(JMethod method) {
        this.method=method;
    }

    public JMethod getMethod() {
        return method;
    }

    @Override
    public JType getReturnType() {
        return method.getReturnType();
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return method.invoke(context);
    }

    @Override
    public JSignature getSignature() {
        return method.getSignature();
    }

    @Override
    public String getSourceName() {
        return method.getSourceName();
    }
}
