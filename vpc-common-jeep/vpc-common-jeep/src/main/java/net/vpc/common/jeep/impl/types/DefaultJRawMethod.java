package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.util.JTypeUtils;

import java.lang.reflect.Modifier;
import java.util.Objects;

public class DefaultJRawMethod extends AbstractJMethod implements JRawMethod{
    private JInvoke handler;
    private JType declaringType;
    private JType returnType;
    private JType genericReturnType;
    private JTypeVariable[] typeParameters=new JTypeVariable[0];
    private JSignature signature;
    private String[] argNames;
    private JSignature genericSignature;
    private int modifiers;

    public DefaultJRawMethod() {
    }

    @Override
    public JType[] argTypes() {
        JSignature s = signature();
        return s==null?null:s.argTypes();
    }


    @Override
    public String[] argNames() {
        return argNames;
    }

    public DefaultJRawMethod setArgNames(String[] argNames) {
        this.argNames = argNames;
        return this;
    }

    @Override
    public JSignature signature() {
        return signature;
    }

    @Override
    public JSignature genericSignature() {
        return genericSignature;
    }

    @Override
    public JType genericReturnType() {
        return genericReturnType;
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(modifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(modifiers());
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers());
    }

    public int modifiers() {
        return modifiers;
    }

    public DefaultJRawMethod setModifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public JInvoke getHandler() {
        return handler;
    }

    public DefaultJRawMethod setHandler(JInvoke handler) {
        this.handler = handler;
        return this;
    }

    public JType declaringType() {
        return declaringType;
    }

    public DefaultJRawMethod setDeclaringType(JType declaringType) {
        this.declaringType = declaringType;
        return this;
    }

    public DefaultJRawMethod setGenericSignature(JSignature signature) {
        this.genericSignature = signature;
        this.signature=JSignature.of(genericSignature.name(),
                JTypeUtils.buildRawType(genericSignature.argTypes(),this),genericSignature.isVarArgs());
        return this;
    }

    public DefaultJRawMethod setGenericReturnType(JType returnType) {
        this.genericReturnType = returnType;
        this.returnType = JTypeUtils.buildRawType(returnType,this);
        return this;
    }

    @Override
    public JType returnType() {
        return returnType;
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return handler.invoke(context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultJRawMethod that = (DefaultJRawMethod) o;
        return Objects.equals(declaringType, that.declaringType) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declaringType, signature);
    }

    public JTypeVariable[] typeParameters() {
        return typeParameters;
    }

    public DefaultJRawMethod setTypeParameters(JTypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
        return this;
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JParameterizedMethodImpl(
                this,parameters,declaringType()
        );
    }

    public boolean isDefault() {
        // Default methods are public non-abstract instance methods
        // declared in an interface.
        return ((modifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) ==
                Modifier.PUBLIC) && declaringType().isInterface();
    }

}
