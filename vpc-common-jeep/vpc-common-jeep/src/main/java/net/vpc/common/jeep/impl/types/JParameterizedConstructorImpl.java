package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.util.JTypeUtils;

public class JParameterizedConstructorImpl extends AbstractJConstructor implements JParameterizedConstructor {
    private JConstructor rawConstructor;
    private JParameterizedType declaringType;
    private JType[] actualTypes;
    private JSignature sig;

    public JParameterizedConstructorImpl(JConstructor rawConstructor, JType[] actualTypes, JParameterizedType declaringType) {
        this.rawConstructor = rawConstructor;
        this.declaringType = declaringType;
        this.actualTypes = actualTypes;
        if (rawConstructor instanceof JRawConstructor) {
            JRawConstructor rawMethod1 = (JRawConstructor) rawConstructor;
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawMethod1.genericSignature().argTypes(), this);
            this.sig = JSignature.of(rawConstructor.name(), jeepParameterTypes);
        } else {
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawConstructor.signature().argTypes(), this);
            this.sig = JSignature.of(rawConstructor.name(), jeepParameterTypes);
        }
    }

    @Override
    public JType[] argTypes() {
        return sig.argTypes();
    }

    @Override
    public String[] argNames() {
        return rawConstructor.argNames();
    }
    @Override
    public JConstructor rawConstructor() {
        return rawConstructor;
    }

    @Override
    public JType[] actualParameters() {
        return actualTypes;
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JType declaringType() {
        return declaringType;
    }

    @Override
    public boolean isPublic() {
        return rawConstructor.isPublic();
    }

    @Override
    public int modifiers() {
        return rawConstructor.modifiers();
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return rawConstructor.invoke(context);
    }

    @Override
    public JSignature signature() {
        return sig;
    }

    @Override
    public JType returnType() {
        return declaringType;
    }

    @Override
    public String name() {
        return declaringType.getRawType().simpleName();
    }

    @Override
    public JDeclaration getDeclaration() {
        return declaringType;
    }
}
