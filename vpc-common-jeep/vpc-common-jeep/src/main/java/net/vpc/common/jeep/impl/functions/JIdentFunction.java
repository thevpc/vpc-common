package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.JInvokeContext;
import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.core.JFunctionBase;

public class JIdentFunction extends JFunctionBase {
    public JIdentFunction(String name, JType type) {
        super(name, type,new JType[0],false);
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        return icontext.evaluate(icontext.getArguments()[0]);
    }

    @Override
    public String getSourceName() {
        return "<runtime>";
    }
}
