package net.thevpc.jeep.impl.vars;

import net.thevpc.jeep.JInvokeContext;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JVar;

public abstract class JVarReadOnly extends AbstractJVar {
    private String name;

    public JVarReadOnly(String name) {
        this.name = name;
    }

    @Override
    public JType undefinedType() {
        return type();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public String name() {
        return name;
    }



    @Override
    public JVar setValue(Object value, JInvokeContext context) {
        throw new IllegalArgumentException("Read only var "+ name());
    }

    @Override
    public boolean isDefinedValue() {
        return true;
    }

    @Override
    public boolean isUndefinedValue() {
        return false;
    }

    @Override
    public JVar setUndefinedValue() {
        throw new IllegalArgumentException("Read only var "+ name());
    }
}
