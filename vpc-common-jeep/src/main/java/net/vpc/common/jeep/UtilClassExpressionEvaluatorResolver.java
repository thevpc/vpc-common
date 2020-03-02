package net.vpc.common.jeep;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class UtilClassExpressionEvaluatorResolver extends AbstractExpressionEvaluatorResolver {

    private final List<Class> importedTypes = new ArrayList<>();
    private final List<Class> importedMethods = new ArrayList<>();
    private final List<Class> importedFields = new ArrayList<>();
    private boolean importsFirst = false;
    private boolean checkEmptyArgMethods = false;

    public UtilClassExpressionEvaluatorResolver(Class... types) {
        this(false, types);
    }

    public UtilClassExpressionEvaluatorResolver(boolean importsFirst, Class... types) {
        this.importsFirst = importsFirst;
        importedTypes.addAll(Arrays.asList(types));
    }

    public boolean isCheckEmptyArgMethods() {
        return checkEmptyArgMethods;
    }

    public void setCheckEmptyArgMethods(boolean checkEmptyArgMethods) {
        this.checkEmptyArgMethods = checkEmptyArgMethods;
    }

    public UtilClassExpressionEvaluatorResolver addImportFields(Class type) {
        importedFields.add(type);
        return this;
    }

    public UtilClassExpressionEvaluatorResolver addImportMethods(Class type) {
        importedMethods.add(type);
        return this;
    }

    @Override
    public Variable resolveVariable(String name, ExpressionManager context) {
        LinkedHashSet<Class> t = new LinkedHashSet<>(importedTypes);
        t.addAll(importedFields);
        //check for fields
        for (Class type : t) {
            Field field = null;
            try {
                field = type.getField(name);
            } catch (Exception ex) {
                //
            }
            if (field != null) {
                return JeepFactory.createStaticFieldVar(field);
            }
        }
        if (checkEmptyArgMethods) {
            Function f = resolveFunction(name, new ExpressionNode[0], null, context);
            if (f != null) {
                return new FunctionVariable(f);
            }
        }
        return null;
    }

    @Override
    public Function resolveFunction(String name, ExpressionNode[] args, ArgsPossibility argPossibility, ExpressionManager evaluator) {
        Function r = resolveFunctionDef0(name, args, argPossibility, evaluator);
        if (r == null) {
            return null;
        }
        ExpressionEvaluatorConverter[] conv = new ExpressionEvaluatorConverter[args.length];
        boolean someConv = false;
        for (int i = 0; i < conv.length; i++) {
            Class to = r.getArgTypes()[i];
            Class from = argPossibility == null ? args[i].getExprType(evaluator) : argPossibility.getConverted(i);
            if (argPossibility != null) {
                if (argPossibility.getConverter(i) != null) {
                    conv[i] = argPossibility.getConverter(i);
                    someConv = true;
                }
            } else {
                if (!to.isAssignableFrom(from)) {
                    conv[i] = JeepUtils.createTypeImplicitConversions(from, to);
                    someConv = true;
                }
            }
        }
        if (someConv) {
            return new ConvertedFunction(r, conv, null);
        } else {
            return r;
        }
    }

    protected Function resolveFunctionDef0(String name, ExpressionNode[] args, ArgsPossibility argPossibility, ExpressionManager evaluator) {
        switch (name) {
            case "": {
                return null;
            }
            case "+": {
                if (args.length == 1) {
                    //unary
                    return JeepFactory.createConstFunction(name, args[0]);
                } else if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "add", "reverseAdd", "add", evaluator);
                }
                break;
            }
            case "-": {
                if (args.length == 1) {
                    //unary
                    Function m = getUnaryExpressionNode(name, args, argPossibility, "neg", "neg", evaluator);
                    if (m != null) {
                        return m;
                    }
                } else if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "sub", "reverseSub", "sub", evaluator);
                }
                break;
            }
            case "*": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "mul", "reverseMul", "mul", evaluator);
                }
                break;
            }
            case "/": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "div", "reverseDiv", "div", evaluator);
                }
                break;
            }
            case "^": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "xor", "reverseXor", "xor", evaluator);
                }
                break;
            }
            case "~": {
                if (args.length == 1) {
                    //unary
                    Function m = getUnaryExpressionNode(name, args, argPossibility, "tilde", "tilde", evaluator);
                    if (m != null) {
                        return m;
                    }
                } else if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "power", "reversePower", "power", evaluator);
                }
                break;
            }
            case "**": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "scalarProduct", "reverseScalarProduct", "scalarProduct", evaluator);
                }
                break;
            }
            case "||": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "or", "reverseOr", "or", evaluator);
                }
                break;
            }
            case "|": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "binaryOr", "reverseBinaryOr", "binaryOr", evaluator);
                }
                break;
            }
            case "&&": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "and", "reverseAnd", "and", evaluator);
                }
                break;
            }
            case "&": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "binaryAnd", "reverseBinaryAnd", "binaryAnd", evaluator);
                }
                break;
            }
            case "->": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "rightArrow", "reverseRightArrow", "rightArrow", evaluator);
                }
                break;
            }
            case "<": {
                if (args.length == 2) {
                    Function n = getBinaryExpressionNode(name, args, argPossibility, "compareTo", "reverseCompareTo", "compare", evaluator);
                    if (n != null) {
                        return new ConvertedFunction(n, null, new AbstractExpressionEvaluatorConverter(Integer.TYPE, Boolean.TYPE) {
                            @Override
                            public Object convert(Object value) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() < 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case "<=": {
                if (args.length == 2) {
                    Function n = getBinaryExpressionNode(name, args, argPossibility, "compareTo", "reverseCompareTo", "compare", evaluator);
                    if (n != null) {
                        return new ConvertedFunction(n, null, new AbstractExpressionEvaluatorConverter(Integer.TYPE, Boolean.TYPE) {
                            @Override
                            public Object convert(Object value) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() <= 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case ">": {
                if (args.length == 2) {
                    Function n = getBinaryExpressionNode(name, args, argPossibility, "compareTo", "reverseCompareTo", "compare", evaluator);
                    if (n != null) {
                        return new ConvertedFunction(n, null, new AbstractExpressionEvaluatorConverter(Integer.TYPE, Boolean.TYPE) {
                            @Override
                            public Object convert(Object value) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() > 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case ">=": {
                if (args.length == 2) {
                    Function n = getBinaryExpressionNode(name, args, argPossibility, "compareTo", "reverseCompareTo", "compare", evaluator);
                    if (n != null) {
                        return new ConvertedFunction(n, null, new AbstractExpressionEvaluatorConverter(Integer.TYPE, Boolean.TYPE) {
                            @Override
                            public Object convert(Object value) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() >= 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case "==": {
                if (args.length == 2) {
                    return getBinaryExpressionNode(name, args, argPossibility, "equals", "equals", "equals", evaluator);
                }
                break;
            }
            case "!=": {
                if (args.length == 2) {
                    Function n = getBinaryExpressionNode(name, args, argPossibility, "equals", "equals", "equals", evaluator);
                    if (n != null) {
                        return new ConvertedFunction(n, null, new AbstractExpressionEvaluatorConverter(Integer.TYPE, Boolean.TYPE) {
                            @Override
                            public Object convert(Object value) {
                                Boolean v = (Boolean) value;
                                return v != null && !v.booleanValue();
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case "<>": {
                if (args.length == 2) {
                    Function n = getBinaryExpressionNode(name, args, argPossibility, "equals", "equals", "equals", evaluator);
                    if (n != null) {
                        return new ConvertedFunction(n, null, new AbstractExpressionEvaluatorConverter(Integer.TYPE, Boolean.TYPE) {
                            @Override
                            public Object convert(Object value) {
                                Boolean v = (Boolean) value;
                                return v != null && !v.booleanValue();
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            default: {
                Class[] argTypes = null;
                if (argPossibility != null) {
                    argTypes = argPossibility.getConverted();
                } else {
                    argTypes = new Class[args.length];
                    for (int i = 0; i < argTypes.length; i++) {
                        argTypes[i] = args[i].getExprType(evaluator);
                    }
                }
                for (Class type : importedTypes) {
                    final Method m = JeepPlatformUtils.getMatchingMethod(type, name, argTypes);
                    if (m != null) {
                        if (m.isVarArgs()) {
                            return new FunctionBase(name, m.getReturnType(), argTypes, importsFirst) {
                                @Override
                                public Object eval(ExpressionNode[] args, ExpressionEvaluator evaluator) {
                                    Object[] all = new Object[m.getParameterTypes().length];
                                    for (int i = 0; i < all.length - 1; i++) {
                                        if (ExpressionNode.class.isAssignableFrom(m.getParameterTypes()[i])) {
                                            all[i] = (args[i]);
                                        } else {
                                            all[i] = args[i].evaluate(evaluator);
                                        }
                                    }
                                    Class last = m.getParameterTypes()[all.length - 1];
                                    int varArgCount = args.length - (all.length - 1);
                                    Object anArray = Array.newInstance(last.getComponentType(), varArgCount);
                                    all[all.length - 1] = anArray;
                                    for (int i = 0; i < varArgCount; i++) {
                                        ExpressionNode aaa = args[all.length - 1 + i];
                                        if (ExpressionNode.class.isAssignableFrom(last.getComponentType())) {
                                            Array.set(anArray, i, aaa);
                                        } else {
                                            Array.set(anArray, i, aaa.evaluate(evaluator));
                                        }
                                    }
                                    try {
                                        m.setAccessible(true);
                                        return m.invoke(null, all);
                                    } catch (IllegalAccessException e) {
                                        throw new IllegalArgumentException(e);
                                    } catch (InvocationTargetException e) {
                                        throw new IllegalArgumentException(e.getTargetException());
                                    } catch (Exception e) {
                                        throw new IllegalArgumentException(e);
                                    }
                                }
                            };
                        }
                        int[] indices = new int[argTypes.length];
                        for (int i = 0; i < argTypes.length; i++) {
                            indices[i] = i;
                        }
                        return createMethodInvocationFunction(name, m, -1, indices);
                    }
                }
            }
        }
        return null;
    }

    private Function getBinaryExpressionNode(String name, ExpressionNode[] args, ArgsPossibility argPossibility, String directName, String reverseName, String staticName, ExpressionManager evaluator) {
        Class arg1 = null;
        Class arg2 = null;
        if (argPossibility != null) {
            arg1 = argPossibility.getConverted(0);
            arg2 = argPossibility.getConverted(1);
        } else {
            arg1 = args[0].getExprType(evaluator);
            arg2 = args[1].getExprType(evaluator);
        }
        int[] order = !(importsFirst) ? new int[]{1, 2} : new int[]{2, 1};
        LinkedHashSet<Class> t = new LinkedHashSet<>(importedTypes);
        t.addAll(importedMethods);
        for (int i = 0; i < order.length; i++) {
            switch (order[i]) {
                case 1: {
                    Method m = JeepPlatformUtils.getMatchingMethod(arg1, directName, arg2);
                    if (m != null) {
                        return createMethodInvocationFunction(name, m, 0, 1);
                    }

                    Method rm = JeepPlatformUtils.getMatchingMethod(arg2, reverseName, arg1);
                    if (rm != null) {
                        return createMethodInvocationFunction(name, rm, 1, 0);
                    }
                    break;
                }
                case 2: {
                    for (Class type : t) {
                        Method sm = JeepPlatformUtils.getMatchingMethod(type, staticName, arg1, arg2);
                        if (sm != null) {
                            return createMethodInvocationFunction(name, sm, -1, 0, 1);
                        }
                    }
                    break;
                }
            }
        }

        return null;
    }

    private Function getUnaryExpressionNode(String name, ExpressionNode[] args, ArgsPossibility argPossibility, String directName, String staticName, ExpressionManager evaluator) {
        Class arg1Type = argPossibility != null ? argPossibility.getConverted(0) : args[0].getExprType(evaluator);
        int[] order = !(importsFirst) ? new int[]{1, 2} : new int[]{2, 1};
        LinkedHashSet<Class> t = new LinkedHashSet<>(importedTypes);
        t.addAll(importedMethods);

        for (int i = 0; i < order.length; i++) {
            switch (order[i]) {
                case 1: {
                    Method m = JeepPlatformUtils.getMatchingMethod(arg1Type, directName);
                    if (m != null) {
                        return createMethodInvocationFunction(name, m, 0);
                    }
                    break;

                }
                case 2: {
                    for (Class type : t) {
                        Method sm = JeepPlatformUtils.getMatchingMethod(type, staticName, arg1Type);
                        if (sm != null) {
                            return createMethodInvocationFunction(name, sm, -1, 0);
                        }
                    }
                    break;
                }
            }
        }

        return null;
    }

    public Function createMethodInvocationFunction(String name, Method m, int baseIndex, int... argIndices) {
        return new MethodInvocationFunction(name, m, baseIndex, argIndices);
    }
}
