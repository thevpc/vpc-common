package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.eval.JEvaluableValue;
import net.vpc.common.jeep.JTypeArray;

import java.util.Objects;

public class JMethodWithVarArg implements JMethod {
    private final JMethod method;

    public JMethodWithVarArg(JMethod method) {
        this.method = method;
    }

    @Override
    public String name() {
        return method.name();
    }

    @Override
    public JType declaringType() {
        return method.declaringType();
    }

    @Override
    public boolean isStatic() {
        return method.isStatic();
    }

    @Override
    public boolean isPublic() {
        return method.isPublic();
    }

    @Override
    public boolean isAbstract() {
        return method.isPublic();
    }

    @Override
    public JSignature signature() {
        return method.signature();
    }

    @Override
    public JType returnType() {
        return method.returnType();
    }

    @Override
    public int modifiers() {
        return method.modifiers();
    }

    @Override
    public JType[] argTypes() {
        return method.argTypes();
    }

    @Override
    public String[] argNames() {
        return method.argNames();
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
                        .name(method.name())
                        .arguments(all)
                        .build()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JMethodWithVarArg that = (JMethodWithVarArg) o;
        return Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method);
    }

    @Override
    public JDeclaration declaration() {
        return declaringType();
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return method.typeParameters();
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JMethodWithVarArg(method.parametrize(parameters));
    }

    @Override
    public boolean isDefault() {
        return method.isDefault();
    }

}
