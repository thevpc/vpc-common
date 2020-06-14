package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.util.JTypeUtils;

public class JParameterizedMethodImpl extends AbstractJMethod implements JParameterizedMethod{
    private JMethod rawMethod;
    private JType declaringType;
    private JType[] actualParameters;
    private JType returnType;
    private JSignature sig;

    public JParameterizedMethodImpl(JMethod rawMethod, JType[] actualParameters,JType declaringType) {
        this.rawMethod=rawMethod;
        this.declaringType = declaringType;
        this.actualParameters=actualParameters;
        if(rawMethod instanceof JRawMethod) {
            JRawMethod rawMethod1 = (JRawMethod) rawMethod;
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawMethod1.genericSignature().argTypes(), this);
            this.returnType = JTypeUtils.buildActualType(rawMethod1.genericReturnType(), this);
            this.sig=JSignature.of(rawMethod.name(),jeepParameterTypes);
        }else{
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawMethod.signature().argTypes(), this);
            this.returnType = JTypeUtils.buildActualType(rawMethod.returnType(), this);
            this.sig=JSignature.of(rawMethod.name(),jeepParameterTypes);
        }
    }

    @Override
    public JType[] argTypes() {
        return sig.argTypes();
    }

    @Override
    public String[] argNames() {
        return rawMethod.argNames();
    }

    @Override
    public JType[] actualParameters() {
        return actualParameters;
    }

    @Override
    public JMethod rawMethod() {
        return rawMethod;
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return rawMethod.invoke(context);
    }

    @Override
    public boolean isAbstract() {
        return rawMethod.isAbstract();
    }

    @Override
    public boolean isStatic() {
        return rawMethod.isStatic();
    }

    @Override
    public boolean isPublic() {
        return rawMethod.isPublic();
    }

    @Override
    public int modifiers() {
        return rawMethod.modifiers();
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
    public JType returnType() {
        return returnType;
    }

    @Override
    public JSignature signature() {
        return sig;
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JParameterizedMethodImpl(
                this,parameters,declaringType()
        );
    }

    @Override
    public boolean isDefault() {
        return rawMethod.isDefault();
    }
}
