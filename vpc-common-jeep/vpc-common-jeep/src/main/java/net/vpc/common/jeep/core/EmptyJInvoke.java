package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JInvoke;
import net.vpc.common.jeep.JInvokeContext;

public class EmptyJInvoke implements JInvoke {
    public static final EmptyJInvoke INSTANCE=new EmptyJInvoke();

    private EmptyJInvoke() {
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return null;
    }
}
