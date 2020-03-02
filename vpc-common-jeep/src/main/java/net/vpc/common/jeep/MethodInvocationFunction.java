package net.vpc.common.jeep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodInvocationFunction extends FunctionBase {

    private final Method m;
    private final int baseIndex;
    private final int[] argIndices;

    public MethodInvocationFunction(String name, Method m, int baseIndex, int... argIndices) {
        super(name, m.getReturnType(), evalParameterTypes(m, baseIndex, argIndices), false);
        this.m = m;
        this.baseIndex = baseIndex;
        if (baseIndex < 0 && !Modifier.isStatic(m.getModifiers())) {
            throw new IllegalArgumentException("Static method expected " + m);
        }
        this.argIndices = Arrays.copyOf(argIndices, argIndices.length);
    }

    private static Class[] evalParameterTypes(Method m, int baseIndex, int[] argIndices) {
        List<Class> all = new ArrayList<>();
        if (baseIndex >= 0) {
            all.add(m.getDeclaringClass());
        }
        for (int i = 0; i < argIndices.length; i++) {
            Class<?>[] parameterTypes = m.getParameterTypes();
            all.add(parameterTypes[i]);
        }
        return all.toArray(new Class[all.size()]);
    }

    @Override
    public Object eval(ExpressionNode[] args, ExpressionEvaluator evaluator) {
        Object[] argsv = null;
        Object base = null;
        try {
            base = baseIndex < 0 ? null : args[baseIndex].evaluate(evaluator);
            argsv = new Object[argIndices.length];
            for (int i = 0; i < argsv.length; i++) {
                ExpressionNode narg = args[argIndices[i]];
                argsv[i] = narg.evaluate(evaluator);
                Class exptected = getArgTypes()[argIndices[i]];
                exptected = JeepPlatformUtils.toBoxingType(exptected);
                if (argsv[i] != null && !exptected.isInstance(argsv[i])) {
                    narg.evaluate(evaluator);
//                    System.out.println("Why");
                }
            }
            return evaluateImpl(base, m, argsv, args, evaluator);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e.getTargetException());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected Object evaluateImpl(Object base, Method m, Object[] argsv, ExpressionNode[] args, ExpressionEvaluator evaluator) throws InvocationTargetException, IllegalAccessException {
        return m.invoke(base, argsv);
    }

}
