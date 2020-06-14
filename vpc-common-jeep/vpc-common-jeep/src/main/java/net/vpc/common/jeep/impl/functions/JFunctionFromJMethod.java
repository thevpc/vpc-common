package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JFunctionBase;
import net.vpc.common.jeep.core.eval.JEvaluableValue;
import net.vpc.common.jeep.JTypeArray;
import net.vpc.common.jeep.util.JTypeUtils;

public class JFunctionFromJMethod extends JFunctionBase {
    private final JMethod method;

    public JFunctionFromJMethod(String name, JMethod method, JTypeOrLambda[] argTypes) {
        //TODO fix me later
        super(name, method.returnType(), JTypeUtils.typesOrError(argTypes), method.signature().isVarArgs());
        this.method = method;
    }

    public JMethod getMethod() {
        return method;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JType[] mTypes = method.signature().argTypes();
        JEvaluable[] all = new JEvaluable[mTypes.length];
        JEvaluable[] args = icontext.arguments();
        for (int i = 0; i < all.length - 1; i++) {
            if (icontext.context().types().forName(JEvaluable.class.getName()).isAssignableFrom(mTypes[i])) {
                all[i] = new JEvaluableValue((args[i]),mTypes[i]);
            } else {
                all[i] = args[i];
            }
        }
        JType last = mTypes[all.length - 1];
        int varArgCount = args.length - (all.length - 1);
        all[all.length - 1] = new JEvaluable() {
            @Override
            public JType type() {
                return icontext.argumentTypes()[icontext.argumentTypes().length-1];
            }

            @Override
            public Object evaluate(JInvokeContext context) {
                JTypeArray jType = (JTypeArray) ((JTypeArray)last).componentType();
                Object anArray0 = jType.newArray(varArgCount);
                JArray anArray = ((JTypeArray)jType.toArray(varArgCount)).asArray(anArray0);
                anArray.value();
                for (int i = 0; i < varArgCount; i++) {
                    JEvaluable aaa = args[all.length - 1 + i];
                    if (icontext.context().types().forName(JEvaluable.class.getName()).isAssignableFrom(jType)) {
                        anArray.set(i, aaa);
                    } else {
                        anArray.set(i, icontext.evaluate(aaa));
                    }
                }
                return anArray.value();
            }
        };
        return method.invoke(
                icontext.builder()
                        .instance(null)
                        .name(method.name())
                        .arguments(all)
                        .build()
        );
    }
}
