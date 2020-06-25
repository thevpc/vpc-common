package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.impl.types.AbstractJConstructor;
import net.vpc.common.jeep.impl.types.JTypesHostHelper;
import net.vpc.common.jeep.util.JTypeUtils;
import net.vpc.common.jeep.util.JeepPlatformUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class HostJRawConstructor extends AbstractJConstructor implements JRawConstructor {
    private Constructor constructor;
    private JType declaringType;
    private JSignature genericSig;
    private JSignature rawSig;
    private JTypeVariable[] typeParameters;
    private String[] argNames;
    private JType[] argTypes;

    public HostJRawConstructor(Constructor constructor, JType declaringType) {
        this.constructor = constructor;
        this.declaringType = declaringType;
        if (!declaringType.isRawType()) {
            throw new IllegalStateException("Error");
        }
        JeepPlatformUtils.setAccessibleWorkaround(constructor);
        for (JTypesResolver resolver : declaringType.types().resolvers()) {
            String s = resolver.resolveConstructorSignature(constructor);
            if (s != null) {
                genericSig = JSignature.of(declaringType.types(), s);
                break;
            }
        }
        if (genericSig == null) {
            JSig sigAnn = (JSig) constructor.getAnnotation(JSig.class);
            if (sigAnn != null) {
                genericSig = JSignature.of(declaringType.types(), sigAnn.value());
            }
        }
        JType[] jeepParameterTypes = getTypesHelper().forName(constructor.getGenericParameterTypes(), this);
        List<String> argNamesList = new ArrayList<>();
        try {
            for (Parameter parameter : constructor.getParameters()) {
                String an = parameter.getName();
                if (an == null) {
                    //just skip
                    throw new IllegalArgumentException();
                }
                argNamesList.add(an);
            }
        } catch (Exception ex) {
            //ignore
        }
        this.argTypes = jeepParameterTypes;
        if (argNamesList.size() == argTypes.length) {
            this.argNames = argNamesList.toArray(new String[0]);
        } else {
            this.argNames = new String[argTypes.length];
            for (int i = 0; i < this.argNames.length; i++) {
                this.argNames[i] = "arg" + (i + 1);
            }
        }
//        genericReturnType=declaringType.types().forName(constructor.getGenericReturnType());
//        rawReturnType=declaringType.types().forName(constructor.getReturnType());
        typeParameters = Arrays.stream(getTypesHelper().forName(constructor.getTypeParameters(), this)).toArray(JTypeVariable[]::new);
        if (genericSig != null) {
            //add some match checking
            if (genericSig.argTypes().length != jeepParameterTypes.length) {
                throw new IllegalArgumentException("Method parameters mismatch");
            }
            for (int i = 0; i < jeepParameterTypes.length; i++) {
                if (!jeepParameterTypes[i].typeName().name().equals(genericSig.argType(i).getName())) {
                    throw new IllegalArgumentException("Method parameters mismatch");
                }
            }
            rawSig = JSignature.of(constructor.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), this), constructor.isVarArgs());
        } else {
            genericSig = new JSignature(constructor.getName(), jeepParameterTypes, constructor.isVarArgs());
            rawSig = JSignature.of(constructor.getName(), JTypeUtils.buildRawType(genericSig.argTypes(), this), constructor.isVarArgs());//just for test
//            rawSig=new JSignature(method.getName(), declaringType.types().forName(method.getParameterTypes()), method.isVarArgs());
        }
    }

    @Override
    public JType[] getArgTypes() {
        return argTypes;
    }

    @Override
    public String[] getArgNames() {
        return argNames;
    }

    protected JTypesHostHelper getTypesHelper() {
        return new JTypesHostHelper(getTypes());
    }

    protected JTypes getTypes() {
        return declaringType.types();
    }

    @Override
    public JSignature getGenericSignature() {
        return genericSig;
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return null;
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    @Override
    public JSignature getSignature() {
        return rawSig;
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(constructor.getModifiers());
    }

    @Override
    public int getModifiers() {
        return constructor.getModifiers();
    }

    @Override
    public JType getReturnType() {
        return getDeclaringType();
    }

    @Override
    public String getName() {
        return declaringType.getName();
    }

    @Override
    public JDeclaration getDeclaration() {
        return getDeclaringType();
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return typeParameters;
    }

    @Override
    public String getSourceName() {
        return "<library>";
    }
}
