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
    private String sourceName;

    public DefaultJRawMethod() {
    }

    @Override
    public JType[] getArgTypes() {
        JSignature s = getSignature();
        return s==null?null:s.argTypes();
    }


    @Override
    public String[] getArgNames() {
        return argNames;
    }

    public DefaultJRawMethod setArgNames(String[] argNames) {
        this.argNames = argNames;
        return this;
    }

    @Override
    public JSignature getSignature() {
        return signature;
    }

    @Override
    public JSignature getGenericSignature() {
        return genericSignature;
    }

    @Override
    public JType getGenericReturnType() {
        return genericReturnType;
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public int getModifiers() {
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

    public JType getDeclaringType() {
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
    public JType getReturnType() {
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

    public JTypeVariable[] getTypeParameters() {
        return typeParameters;
    }

    public DefaultJRawMethod setTypeParameters(JTypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
        return this;
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JParameterizedMethodImpl(
                this,parameters, getDeclaringType()
        );
    }

    public boolean isDefault() {
        // Default methods are public non-abstract instance methods
        // declared in an interface.
        return ((getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) ==
                Modifier.PUBLIC) && getDeclaringType().isInterface();
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    public DefaultJRawMethod setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }
}
