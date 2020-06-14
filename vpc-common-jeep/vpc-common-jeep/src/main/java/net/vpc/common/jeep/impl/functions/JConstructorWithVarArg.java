package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.eval.JEvaluableValue;
import net.vpc.common.jeep.impl.types.AbstractJConstructor;
import net.vpc.common.jeep.JTypeArray;

public class JConstructorWithVarArg extends AbstractJConstructor {
    private final JConstructor ctr;

    public JConstructorWithVarArg(JConstructor ctr) {
        this.ctr = ctr;
    }

    @Override
    public String name() {
        return ctr.name();
    }

    @Override
    public JType declaringType() {
        return ctr.declaringType();
    }

    @Override
    public boolean isPublic() {
        return ctr.isPublic();
    }

    @Override
    public JSignature signature() {
        return ctr.signature();
    }

    @Override
    public JType returnType() {
        return ctr.returnType();
    }

    public JConstructor getCtr() {
        return ctr;
    }

    @Override
    public JType[] argTypes() {
        return ctr.argTypes();
    }

    @Override
    public int modifiers() {
        return ctr.modifiers();
    }

    @Override
    public String[] argNames() {
        return ctr.argNames();
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JType[] mTypes = ctr.signature().argTypes();
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
        return ctr.invoke(
                icontext.builder()
                        .name(ctr.name())
                        .arguments(all)
                        .build()
        );
    }

    @Override
    public JDeclaration declaration() {
        return declaringType();
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return ctr.typeParameters();
    }
}
