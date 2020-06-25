/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.functions;

//    public Object evaluate(String expression) {

import net.vpc.common.jeep.*;

import java.util.Objects;

////        return JSharedContext.invokeSilentCallable(this, new JSilentCallable<Object>() {
////            @Override
////            public Object call() {
////                JNode o = parse(expression);
////                if (o == null) {
////                    return null;
////                }
////                return o.evaluate(DefaultJeep.this);
////            }
////        });
//        createEvaluator()
//        JNode n = parse(expression);
//        return evaluate(n);
//
//    }
public class JFunctionFromVariable implements JFunction {

    private final JVar v;
    private final JSignature signature;

    public JFunctionFromVariable(JVar v) {
        this.v = v;
        this.signature = new JSignature(v.name(), new JType[0], false);
    }

    @Override
    public JType getReturnType() {
        return v.type();
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return v.getValue(context);
    }

    @Override
    public String getName() {
        return v.name();
    }

    @Override
    public JSignature getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return signature.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JFunctionFromVariable that = (JFunctionFromVariable) o;
        return Objects.equals(v, that.v) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v, signature);
    }

    @Override
    public String getSourceName() {
        return "<runtime>";
    }

}
