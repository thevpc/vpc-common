package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JFunction;
import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JTypes;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.util.JTypeUtils;

public abstract class JFunctionBase implements JFunction {

    private String name;
    private JType resultType;
    private JType[] argTypes;
    private boolean varArgs;
    private JSignature signature;

    public JFunctionBase(String name, String returnType, String[] argTypes, JTypes types) {
        this(name, types.forName(returnType), types.forName(argTypes), false);
    }

//    public JFunctionBase(String name, Class returnType, Class[] argTypes, JTypes types) {
//        this(name, types.forName(returnType), types.forName(argTypes), false);
//    }

    public JFunctionBase(String name, JType returnType, JType[] argTypes) {
        this(name, returnType, argTypes, false);
    }

    public JFunctionBase(String name, JType returnType, JType[] argTypes, boolean varArgs) {
        this.resultType = returnType;
        this.name = name;
        this.argTypes = argTypes;
        this.varArgs = varArgs;
        this.signature = new JSignature(name, argTypes, varArgs);
        if (varArgs && (this.argTypes.length == 0 || !this.argTypes[this.argTypes.length - 1].isArray())) {
            throw new IllegalArgumentException("Invalid varargs");
        }
    }

    public JFunction getBase() {
        return null;
    }

    @Override
    public JType getReturnType() {
        return resultType;
    }


    public String getName() {
        return name;
    }

    @Override
    public JSignature getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        String n = getName();
        StringBuilder sb = new StringBuilder(n).append("(");
        for (int i = 0; i < argTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String sargi = JTypeUtils.getSimpleClassName(argTypes[i]);
            sb.append(sargi);
        }
        sb.append(")");
        return sb.toString();
    }
}
