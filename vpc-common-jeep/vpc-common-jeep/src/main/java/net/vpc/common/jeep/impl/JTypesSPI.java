package net.vpc.common.jeep.impl;

import net.vpc.common.jeep.JDeclaration;
import net.vpc.common.jeep.JParameterizedType;
import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JTypes;
import net.vpc.common.jeep.impl.types.DefaultJTypes;

public interface JTypesSPI {
    static JType getAlreadyRegistered(String name, JTypes jtypes){
        JTypes types = jtypes;
        while(types.parent()!=null){
            types=types.parent();
        }
        return ((JTypesSPI)types).getRegistered(name);
    }

    static JType getRegisteredOrRegister(JType type, JTypes jtypes){
        JType old = getAlreadyRegistered(type.name(),jtypes);
        if(old!=null){
            return old;
        }
        JTypes types = jtypes;
        while(types.parent()!=null){
            types=types.parent();
        }
        ((JTypesSPI)types).registerType(type);
        return type;
    }

    JType getRegisteredOrAlias(String jt);

    JType getRegistered(String jt);

    void registerType(JType jt);

    JType createArrayType0(JType root, int dim);

    JType createNullType0();

    JType createHostType0(String name);

    JType createMutableType0(String name);

    JType createVarType0(String name, JType[] lowerBounds, JType[] upperBounds, JDeclaration declaration);

    JParameterizedType createParameterizedType0(JType rootRaw, JType[] parameters, JType declaringType);

    ClassLoader hostClassLoader();
}