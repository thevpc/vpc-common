package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.vars.AbstractJVar;
import net.vpc.common.jeep.util.JTypeUtils;

public class JFunctionJVar extends AbstractJVar {

    private final JFunction fct;
    private final JContext context;

    public JFunctionJVar(JFunction fct, JContext context) {
        this.fct = fct;
        this.context = context;
    }

    @Override
    public JType undefinedType() {
        return JTypeUtils.forObject(context.types());
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String name() {
        return fct.name();
    }

    @Override
    public JType type() {
        return fct.returnType();
//                context.types().forName(Object.class);
    }

//    @Override
//    public JType getEffectiveType(JContext context) {
//        return fct.returnType();
//    }

    @Override
    public Object getValue(JContext context) {
        return fct.invoke(
                new DefaultJInvokeContext(
                        context,
                        context.evaluators().newEvaluator(),//???
                        null,
                        new JEvaluable[0],
                        null
                )
        );
    }

    @Override
    public JVar setValue(Object value, JContext context) {
        throw new UnsupportedOperationException("Not supported");
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
        throw new UnsupportedOperationException("Not supported");
    }
}
