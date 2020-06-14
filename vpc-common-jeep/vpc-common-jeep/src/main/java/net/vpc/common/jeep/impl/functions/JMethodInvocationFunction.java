package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JFunctionBase;
import net.vpc.common.jeep.core.DefaultJTypedValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JMethodInvocationFunction extends JFunctionBase {

    private final JMethod method;
    private final int baseIndex;
    private final int[] argIndices;

    public JMethodInvocationFunction(String name, JMethod method, int baseIndex, int... argIndices) {
        super(name, method.returnType(), evalParameterTypes(method, baseIndex, argIndices), false);
        this.method = method;
        this.baseIndex = baseIndex;
        if (baseIndex < 0 && !method.isStatic()) {
            throw new IllegalArgumentException("Static method expected " + method);
        }
        this.argIndices = Arrays.copyOf(argIndices, argIndices.length);
    }


    public JMethod getMethod() {
        return method;
    }

    public int getBaseIndex() {
        return baseIndex;
    }

    public int[] getArgIndices() {
        return argIndices;
    }

    private static JType[] evalParameterTypes(JMethod m, int baseIndex, int[] argIndices) {
        List<JType> all = new ArrayList<>();
        if (baseIndex >= 0) {
            all.add(m.declaringType());
        }
        for (int i = 0; i < argIndices.length; i++) {
            JType[] parameterTypes = m.signature().argTypes();
            all.add(parameterTypes[i]);
        }
        return all.toArray(new JType[0]);
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JEvaluable[] baseArgs = icontext.arguments();
        JEvaluable[] argsv = null;
        JType[] oldTypes = icontext.argumentTypes();
        JTypedValue newInstance=null;
//        Object base = null;
//        JType baseType = null;
        try {
            if(baseIndex < 0) {
                newInstance = icontext.instance();
            }else{
                newInstance=new DefaultJTypedValue(
                        icontext.evaluate(baseArgs[baseIndex]),
                        oldTypes[baseIndex]
                );
            }
            JType[] newTypes=new JType[argIndices.length];
            argsv = new JEvaluable[argIndices.length];
            for (int i = 0; i < argsv.length; i++) {
                argsv[i] = baseArgs[argIndices[i]];
                newTypes[i] = oldTypes[argIndices[i]];
            }
            return evaluateImpl(method,
                    icontext.builder()
                            .instance(newInstance)
                            .arguments(argsv)
                            .name(method.name())
                    .build()
            );
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e.getTargetException());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected Object evaluateImpl(JMethod m, JInvokeContext icontext) throws InvocationTargetException, IllegalAccessException {
        return m.invoke(icontext);
    }

}