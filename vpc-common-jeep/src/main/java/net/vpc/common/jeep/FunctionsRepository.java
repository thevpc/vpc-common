/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.util.*;

/**
 * @author vpc
 */
public class FunctionsRepository {

    private final Map<String, String> operatorsAliases = new HashMap<>();

    private final Map<MethodSignature, Function> functionsBySig = new HashMap<>();
    private final Map<String, Function> functionsByName = new HashMap<>();
    private final Map<MethodSignature, Function> functionsBySigCache = new HashMap<>();

    private final Map<Integer, List<MethodSignature>> sigsByOpCount = new HashMap<>();
    private final Map<String, Long> opPrecedence = new HashMap<>();
    private final Set<String> abstractBinaryOpNames = new HashSet<>();
    private final Set<String> abstractUnaryOpNames = new HashSet<>();
    private final Map<String, String> canonicalToAbsolute = new HashMap<>();
    private boolean caseInsensitive;

    public FunctionsRepository(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    void declareUnaryOperator(String operator) {
        abstractUnaryOpNames.add(getCanonicalName(operator));
    }

    void declareBinaryOperator(String operator) {
        abstractBinaryOpNames.add(getCanonicalName(operator));
    }

    public boolean containsBinaryOperator(String operator) {
        return abstractBinaryOpNames.contains(getCanonicalName(operator));
    }

    public boolean containsUnaryOperator(String operator) {
        return abstractUnaryOpNames.contains(getCanonicalName(operator));
    }

    public boolean containsOperator(String operator) {
        String canonicalName = getCanonicalName(operator);
        return abstractUnaryOpNames.contains(canonicalName) || abstractUnaryOpNames.contains(canonicalName);
    }

    void declareUnaryOperator(String operator, long precedence) {
        declareUnaryOperator(operator);
        setOperatorPrecedence(operator, precedence);
    }

    void declareBinaryOperator(String operator, long precedence) {
        declareBinaryOperator(operator);
        setOperatorPrecedence(operator, precedence);
    }

    void setOperatorPrecedence(String operator, long precedence) {
        opPrecedence.put(getCanonicalName(operator), precedence);
    }

    void unsetOperatorPrecedence(String operator, long precedence) {
        opPrecedence.remove(getCanonicalName(operator));
    }

    boolean isUnaryOperator(String op) {
        String canonicalName = getCanonicalName(op);
        if (abstractUnaryOpNames.contains(canonicalName)) {
            return true;
        }

        String r = getRootOperatorName(canonicalName);
        if (abstractUnaryOpNames.contains(r)) {
            return true;
        }
//        List<Function> all = getFunctionsListByName(r);
//        for (Function exprOpDef : all) {
//            switch (exprOpDef.getArgTypes().length) {
//                case 1: {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    boolean isBinaryOperator(String op) {
        String canonicalName = getCanonicalName(op);
        if (abstractBinaryOpNames.contains(canonicalName)) {
            return true;
        }

        String r = getRootOperatorName(canonicalName);
        if (abstractBinaryOpNames.contains(r)) {
            return true;
        }
        List<Function> all = getFunctionsListByName(r);
        for (Function exprOpDef : all) {
            switch (exprOpDef.getArgTypes().length) {
                case 2: {
                    return true;
                }
            }
        }
        return false;
    }

    long getOperatorPrecedence(String op) {
        Long found = opPrecedence.get(getCanonicalName(op));
        if (found != null) {
            return found;
        }
        return JeepUtils.getDefaultBinaryOpPrecedence(getCanonicalName(op));
    }

    void declareOperator(Function operator) {
        String canonicalName = getCanonicalName(operator.getName());
        MethodSignature sig = new MethodSignature(canonicalName, operator.getArgTypes(), operator.isVarArgs());
        functionsBySigCache.clear();
        functionsBySig.put(sig, operator);
        List<MethodSignature> list = sigsByOpCount.get(operator.getArgTypes().length);
        if (list == null) {
            list = new ArrayList<>();
            sigsByOpCount.put(operator.getArgTypes().length, list);
        }
        list.add(sig);
    }

    void declareOperatorAlias(String alias, String referenceOp, Class[] operands, boolean varArgs) {
        String canonicalName = getCanonicalName(referenceOp);
        MethodSignature sig = new MethodSignature(canonicalName, operands, varArgs);
        String aliasCanonicalName = getCanonicalName(alias);
//        MethodSignature aliasSig = new MethodSignature(aliasCanonicalName, operands);
        JeepUtils.validateOperator(alias);
        Function u = functionsBySig.get(sig);
        if (u == null
                && !abstractBinaryOpNames.contains(canonicalName)
                && !abstractUnaryOpNames.contains(canonicalName)
                && !operatorsAliases.containsKey(canonicalName)) {
            throw new IllegalArgumentException("operator not found : " + referenceOp);
        }
        if (operatorsAliases.containsKey(aliasCanonicalName)) {
//            String s2 = operatorsAliases.get(aliasCanonicalName);
//            if (!canonicalName.equals(canonicalName)) {
//                throw new IllegalArgumentException("Alias '" + alias + "' already bound to : " + u.getOp().getName());
//            } else {
//                //ignore !!
//            }
        } else {
            operatorsAliases.put(aliasCanonicalName, canonicalName);
            functionsBySigCache.clear();
        }
    }

    void undeclareOperator(String op, Class[] operands) {
        MethodSignature asig1 = new MethodSignature(getCanonicalName(op), operands, true);
        MethodSignature asig2 = new MethodSignature(getCanonicalName(op), operands, true);
        for (MethodSignature asig : new MethodSignature[]{asig1, asig2}) {
            Function oo = functionsBySig.get(asig);
            if (oo != null) {
                for (Map.Entry<String, String> entry : new HashMap<String, String>(operatorsAliases).entrySet()) {
                    if (entry.getValue().equals(op)) {
                        undeclareOperatorAlias(entry.getKey(), operands);
                    }
                }
                functionsBySig.remove(asig);
                List<MethodSignature> list = sigsByOpCount.get(oo.getArgTypes().length);
                if (list != null) {
                    list.remove(asig);
                }
                functionsBySigCache.clear();
            }
            abstractUnaryOpNames.remove(getCanonicalName(op));
            abstractBinaryOpNames.remove(getCanonicalName(op));
        }
    }

    public String getRootOperatorName(String alias) {
        String a = getCanonicalName(alias);
        int maxDepth = 100;
        HashSet<String> visited = new HashSet<>();
        while (true) {
            String old = operatorsAliases.get(a);
            if (old == null) {
                return a;
            } else {
                visited.add(a);
                a = old;
                maxDepth--;
                if (maxDepth < 0) {
                    throw new IllegalArgumentException("Cycle ref detected for alias " + alias + " -> " + visited);
                }
            }
        }
    }

    void undeclareOperatorAlias(String alias, Class[] operands) {
        String canonicalName = getCanonicalName(alias);
        operatorsAliases.remove(canonicalName);
        functionsBySigCache.clear();
        abstractUnaryOpNames.remove(canonicalName);
        abstractBinaryOpNames.remove(canonicalName);
    }

//    ExpressionOperator getOperator(String name, Class[] operandTypes, ExpressionManager definition) {
//        ExpressionOperator o = findFunction(name, operandTypes, definition);
//        if (o == null) {
//            throw new NoSuchElementException("Unknown operator");
//        }
//        return o;
//    }

    Function findFunction(String name, ExpressionNode[] operands, final ExpressionManager expressionManager) {
        Function n = functionsByName.get(name);
        if (n != null) {
            return n;
        }

        Class[] operandTypes = ((DefaultExpressionManager) expressionManager).getTypes(operands);
        Set<MethodSignature> fails=new HashSet<>();

        Function f = findFunctionNoImplicit(name, operandTypes, expressionManager, fails);
        if(f!=null){
            return f;
        }


        ArgsPossibility[] argsPossibilities = ArgsPossibility.allOf(operandTypes, new ArgsPossibility.ImplicitSupplier() {
            @Override
            public ExpressionEvaluatorConverter[] get(Class clazz) {
                return getExpressionEvaluatorConverters(clazz, expressionManager);
            }
        });
        Set<String> names = getAllNames(name);
        for (ArgsPossibility argsPossibility : argsPossibilities) {
            f = findFunctionNoImplicit(name, argsPossibility.getConverted(), expressionManager, fails);
            if(f!=null){
                return new ConvertedFunction(f,argsPossibility.getConverters(),null);
            }
            for (ExpressionEvaluatorResolver resolver : expressionManager.getResolvers()) {
                if (resolver != null) {
                    for (String nn : names) {
                        Function resolved = resolver.resolveFunction(nn, operands, argsPossibility, expressionManager);
                        if (resolved != null) {
                            return resolved;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void declareFunction(Function function) {
        JeepUtils.validateOperator(function.getName());
        functionsByName.put(getCanonicalName(function.getName()), function);
    }

    public void declareFunction(String name, Class[] args, Class returnType, boolean varArgs, FunctionHandler function) {
        declareFunction(new FunctionForHandler(name, returnType, args, varArgs, function));
    }

    public void undeclareFunction(String name) {
        functionsByName.remove(getCanonicalName(name));
    }

    Function findFunctionNoImplicit(String name, Class[] operandTypes, ExpressionManager definition, Set<MethodSignature> fails) {
        if (fails == null) {
            fails = new HashSet<>();
        }
        MethodSignature k0 = new MethodSignature(getCanonicalName(name), operandTypes, false);
        Function cached = functionsBySigCache.get(k0);
        if (cached != null) {
            //System.out.println("[Cached] findFunction(" + k0 + ")");
            return cached;
        }
        //System.out.println("findFunction(" + k0 + ")");
        if (fails.contains(k0)) {
            return null;
        }
        Function d = null;
        d = functionsBySig.get(k0);
        if (d != null) {
            functionsBySigCache.put(k0, d);
            return d;
        }
        List<Function> allPossibilities = getFunctionsListByName(name);

        MethodObject<Function>[] mo = new MethodObject[allPossibilities.size()];
        for (int i = 0; i < mo.length; i++) {
            Function p = allPossibilities.get(i);
            mo[i] = new MethodObject<>(p.getSignature(), p);
        }
        MethodObject<Function> matchingMethod = JeepPlatformUtils.getMatchingMethod(mo);
        if (matchingMethod != null) {
            d = matchingMethod.getMethod();
        }
        if (d != null) {
            functionsBySigCache.put(k0, d);
            return d;
        }
        fails.add(k0);
        return null;
    }

    private ExpressionEvaluatorConverter[] getExpressionEvaluatorConverters(Class operandType, ExpressionManager definition) {
        LinkedHashSet<ExpressionEvaluatorConverter> allImplicitConverters = new LinkedHashSet<>();
        allImplicitConverters.add(null);
        for (ExpressionEvaluatorConverter ic : JeepUtils.getTypeImplicitConversions(operandType)) {
            if(ic!=null){
                allImplicitConverters.add(ic);
            }
        }
        for (ExpressionEvaluatorResolver resolver : definition.getResolvers()) {
            ExpressionEvaluatorConverter[] next = resolver.resolveImplicitConverters(operandType);
            if (next != null) {
                for (ExpressionEvaluatorConverter ic : next) {
                    allImplicitConverters.add(ic);
                }
            }
        }
        return allImplicitConverters.toArray(new ExpressionEvaluatorConverter[0]);
    }

    static Class[] convertArgumentTypesByIndex(Class[] operandTypes, ExpressionEvaluatorConverter currentConverter, int index) {
        Class[] operandTypes2 = new Class[operandTypes.length];
        for (int j = 0; j < operandTypes2.length; j++) {
            if (index == j) {
                operandTypes2[j] = currentConverter.getTargetType();
            } else {
                operandTypes2[j] = operandTypes[j];
            }
        }
        return operandTypes2;
    }

    public String getCanonicalName(String name) {
        if (caseInsensitive) {
            String canonical = name.toUpperCase();
            String old = canonicalToAbsolute.get(canonical);
            if (old == null) {
                canonicalToAbsolute.put(canonical, name);
                return canonical;
            } else if (old.equals(name)) {
                //okkay
                return canonical;
            } else {
                throw new IllegalArgumentException("Clash of names " + name + " <> " + old);
            }
        }
        return name;
    }

    public List<Function> getFunctionsListByName(String name) {
        List<Function> all = new ArrayList<>();
        for (Function value : getFunctionsMapByName(name).values()) {
            all.add(value);
        }
        return all;
    }

    public Map<MethodSignature, Function> getFunctionsMapByName(String name) {
        name = getCanonicalName(name);
        Map<MethodSignature, Function> all = new HashMap<>();
        for (Map.Entry<MethodSignature, Function> entry : functionsBySig.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(name)) {
                all.put(entry.getKey(), entry.getValue());
            }
        }
        return all;
    }

    public String getOriginalName(String name) {
        if (caseInsensitive) {
            String o = canonicalToAbsolute.get(name);
            if (o != null) {
                return o;
            }
        }
        return name;
    }

    public Set<String> getAllNames(String name) {
        Map<String, Set<String>> c = buildCache();
        Set<String> v = c.get(name);
        if (v == null) {
            v = new LinkedHashSet<>();
            v.add(name);
        }
        return v;
    }

    private Map<String, Set<String>> buildCache() {
        Map<String, Set<String>> cache = new HashMap<>();
        for (Map.Entry<String, String> entry : operatorsAliases.entrySet()) {
            String a = entry.getKey();
            Set<String> g1 = cache.get(a);
            String b = entry.getValue();
            Set<String> g2 = cache.get(b);
            LinkedHashSet<String> g = new LinkedHashSet<>();
            g.add(getOriginalName(a));
            g.add(getOriginalName(b));
            if (g1 != null) {
                g.addAll(g1);
            }
            if (g2 != null) {
                g.addAll(g2);
            }
            for (String s : g) {
                cache.put(s, g);
            }
        }
        return cache;
    }

}
