/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.functions;

//    public Object evaluate(String expression) {

import net.vpc.common.jeep.*;

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
    public JType returnType() {
        return v.type();
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return v.getValue(context.context());
    }

    @Override
    public String name() {
        return v.name();
    }

    @Override
    public JSignature signature() {
        return signature;
    }
    
}
