/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import net.vpc.common.jeep.nodes.ExpressionNodeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <i>Mathematic expression evaluator.</i> Supports the following functions: +,
 * -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max,
 * ceil, floor, absdbl, neg, rndr.<br>
 * <pre>
 * Sample:
 * MathEvaluator m = new MathEvaluator();
 * m.declare("x", 15.1d);
 * System.out.println( m.evaluate("-5-6/(-2) + sqr(15+x)") );
 * </pre>
 *
 * @author Taha BEN SALAH
 * @version 1.0
 * @date April 2008
 */
public class DefaultExpressionManager extends AbstractExpressionManager {

    private final Map<String, Variable> variables = new HashMap<>();
    private final Map<String, String> variablesAliases = new HashMap<>();
    private final List<ExpressionEvaluatorResolver> resolvers = new ArrayList<>();
    private final FunctionsRepository functionsRepository;
    private ExpressionStreamTokenizerConfig tokenizerConfig = null;
    private boolean caseInsensitive = false;

    /**
     * *
     * creates an empty MathEvaluator. You need to use setExpression(String s)
     * to assign a math expression string to it.
     */
    public DefaultExpressionManager() {
        this(false);
    }

    public DefaultExpressionManager(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
        functionsRepository = new FunctionsRepository(caseInsensitive);
    }

    public String getCanonicalName(String name) {
        if (caseInsensitive) {
            return name.toUpperCase();
        }
        return name;
    }

    @Override
    public ExpressionStreamTokenizerConfig getTokenizerConfig() {
        return tokenizerConfig;
    }

    @Override
    public void setTokenizerConfig(ExpressionStreamTokenizerConfig tokenizerConfig) {
        this.tokenizerConfig = tokenizerConfig;
    }

    @Override
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public ExpressionManager declareVarAlias(String alias, String name) {
        variablesAliases.put(getCanonicalName(alias), getCanonicalName(name));
        return this;
    }

    public ExpressionManager undeclareVarAlias(String alias) {
        variablesAliases.remove(getCanonicalName(alias));
        return this;
    }

    public Variable declareConst(String name, Object value) {
        return declareConst(name, value == null ? Object.class : value.getClass(), value);
    }

    public Variable declareConst(String name, Class type, Object value) {
        JeepUtils.validateOperator(name);
        Variable v = declareVar(name, type, type, value);
        v.setReadOnly(true);
        return v;
    }

    @Override
    public void declareVar(Variable def) {
        JeepUtils.validateOperator(def.getName());
        variables.put(getCanonicalName(def.getName()), def);
        invalidateVarCache(def.getName());
    }

    public Variable declareVar(String name, Class type, Object value) {
        JeepUtils.validateOperator(name);
        return declareVar(name, type, type, value);
    }

    public Variable declareVar(String name, Class type, Class undefinedType, Object value) {
        JeepUtils.validateOperator(name);
        Variable value2 = new DefaultVariable(name, type, undefinedType, value);
        declareVar(value2);
        return value2;
    }

    public void declareFunction(Function function) {
        functionsRepository.declareFunction(function);
        invalidateFunctionsCache(function.getSignature());
    }

    public void declareFunction(String name, Class[] args, Class returnType, boolean varArgs, FunctionHandler function) {
        functionsRepository.declareFunction(name, args, returnType, varArgs, function);
        invalidateFunctionsCache(new MethodSignature(name, args, varArgs));
    }

    public void undeclareVar(String name) {
        variables.remove(getCanonicalName(name));
        invalidateVarCache(name);
    }

    public void undeclareFunction(String name) {
        functionsRepository.undeclareFunction(name);
        invalidateFunctionsCache(name);
    }

//    public FunctionOperator getOperator(String name, Class[] operandTypes) {
//        return functionsRepository.getOperator(name, operandTypes, this);
//    }
//    public FunctionOperator findFunction(String name, Class[] operandTypes) {
//        return functionsRepository.findFunction(name, operandTypes, this);
//    }
    public boolean isUnaryOperator(String op) {
        return functionsRepository.isUnaryOperator(op);
    }

    public boolean isListSeparator(String op) {
        return ",".equals(op);
    }

    public boolean isBinaryOperator(String op) {
        return functionsRepository.isBinaryOperator(op);
    }

    public long getOperatorPrecedence(String op) {
        return functionsRepository.getOperatorPrecedence(op);
    }

    public Class[] getTypes(ExpressionNode[] args) {
        if (args == null) {
            return null;
        }
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i] = args[i].getExprType(this);
        }
        return argTypes;
    }

    private String toStr(String name, ExpressionNode... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (args != null) {
            sb.append("(");
            for (int i = 0, argsLength = args.length; i < argsLength; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                ExpressionNode arg = args[i];
                sb.append(arg);
            }
            sb.append(")");
        }
        return sb.toString();
    }


    private Map<MethodSignature, Function> cacheFunctions = new HashMap<>();
    private Map<String, Variable> cacheVars = new HashMap<>();

    public Function findFunction(String name, ExpressionNode... args) {
        Class[] argTypes = args == null ? null : new Class[args.length];
        if (args != null && argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = args[i].getExprType(this);
            }
        }
        MethodSignature methodSignature = new MethodSignature(name, argTypes, false);
        Function p = cacheFunctions.get(methodSignature);
        if (p != null) {
            return p;
        }
        if (cacheFunctions.containsKey(methodSignature)) {
            return null;
        }
//        System.out.println("findFunction " + name + (argTypes == null ? "" : Arrays.toString(argTypes)));

        if (argTypes == null /*|| argTypes.length == 0*/) {
            Variable v = findVariable(name);
            if (v != null) {
                return new FunctionFromVariable(v);
            }
        }
        if (args == null) {
            args = new ExpressionNode[0];
        }
        Function op2 = functionsRepository.findFunction(name, args, this);
        if (op2 != null) {
            cacheFunctions.put(methodSignature, op2);
            return op2;
        }
        return null;
    }

    public void invalidateFunctionsCache(String name) {
        for (Iterator<MethodSignature> it = cacheFunctions.keySet().iterator(); it.hasNext();) {
            MethodSignature k = it.next();
            if (k.getName().equals(name)) {
                it.remove();
            }
        }
    }

    public void invalidateFunctionsCache(MethodSignature signature) {
        if (signature.isVarArgs()) {
            invalidateFunctionsCache(signature.getName());
        } else {
            cacheFunctions.remove(signature);
        }
    }

    public void invalidateFunctionsCache() {
        cacheFunctions.clear();
    }

    public void invalidateVarCache(String name) {
        cacheVars.remove(getCanonicalName(name));
    }

    public void invalidateVarCache() {
        cacheVars.clear();
    }

//    Object getVariableValue(String varName, ExpressionManager evaluator) {
//        Variable v = getVariable(varName);
//        return v.getValue(evaluator);
//    }
    public ExpressionNode getVariableNode(String var) {
        ExpressionNode v = findVariableNode(var);
        if (v == null) {
            throw new NoSuchVariableException(var);
        }
        return v;
    }

    public ExpressionNode findVariableNode(String var) {
        Variable ff = findVariable(var);
        if (ff != null) {
            if (ff.isUndefinedValue()) {
                return new ExpressionNodeVariableName(ff.getName(), ff.getUndefinedType());
            } else {
                return new ExpressionNodeVariableName(ff.getName(), ff.getType());
//                return new ExpressionNodeVariableName(ff.getName(), ff.getEffectiveType(this));
            }
        }
        return null;
    }

    public Variable findVariable(String var) {
        Variable p = cacheVars.get(var);
        if (p != null) {
            return p;
        }
        if (cacheVars.containsKey(var)) {
            return null;
        }

        Variable ff = variables.get(getCanonicalName(var));
        if (ff != null) {
            cacheVars.put(var, ff);
            return ff;
        }
        if (ff == null) {
            String ref = variablesAliases.get(getCanonicalName(var));
            if (ref != null) {
                ff = findVariable(getCanonicalName(ref));
                if (ff != null) {
                    cacheVars.put(var, ff);
                    return ff;
                }
            }
        }
        for (ExpressionEvaluatorResolver resolver : resolvers) {
            if (resolver != null) {
                ff = resolver.resolveVariable(var, this);
                if (ff != null) {
                    cacheVars.put(var, ff);
                    return ff;
                }
            }
        }
        cacheVars.put(var, null);
        return null;
    }

    public Variable getVariable(String var) {
        Variable ff = findVariable(var);
        if (ff == null) {
            throw new NoSuchVariableException(var);
        }
        return ff;
    }

    public ExpressionEvaluatorResolver[] getResolvers() {
        return resolvers.toArray(new ExpressionEvaluatorResolver[0]);
    }

    public ExpressionManager configureDefaults() {
        declareUnaryOperators("-", "!", "~");
        declareBinaryOperators("+", "-", "*", "/", "<", "<=", ">", ">=", "=", "!=");
        importType(PlatformHelper.class);
        return this;
    }

    @Override
    public ExpressionManager importType(Class type) {
        return addResolver(new UtilClassExpressionEvaluatorResolver(type));
    }

    @Override
    public ExpressionManager importMethods(Class type) {
        return addResolver(new UtilClassExpressionEvaluatorResolver().addImportMethods(type));
    }

    @Override
    public ExpressionManager importFields(Class type) {
        return addResolver(new UtilClassExpressionEvaluatorResolver().addImportFields(type));
    }

    @Override
    public ExpressionManager addResolver(ExpressionEvaluatorResolver resolver) {
        this.resolvers.add(resolver);
        return this;
    }

    @Override
    public ExpressionManager removeResolver(ExpressionEvaluatorResolver resolver) {
        this.resolvers.remove(resolver);
        return this;
    }

    public ExpressionManager declareUnaryOperators(String... operator) {
        for (String o : operator) {
            declareUnaryOperator(o);
        }
        return this;
    }

    public ExpressionManager declareBinaryOperators(String... operator) {
        for (String o : operator) {
            declareBinaryOperator(o);
        }
        return this;
    }

    public ExpressionManager declareUnaryOperator(String operator) {
        functionsRepository.declareUnaryOperator(operator);
        invalidateFunctionsCache(operator);
        return this;
    }

    public ExpressionManager declareUnaryOperator(String operator, long precedence) {
        functionsRepository.declareUnaryOperator(operator, precedence);
        invalidateFunctionsCache(operator);
        return this;
    }

    public ExpressionManager declareBinaryOperator(String operator, long precedence) {
        functionsRepository.declareBinaryOperator(operator, precedence);
        invalidateFunctionsCache(operator);
        return this;
    }

    public ExpressionManager declareBinaryOperator(String operator) {
        functionsRepository.declareBinaryOperator(operator);
        invalidateFunctionsCache(operator);
        return this;
    }

    @Override
    public ExpressionManager declareListOperator(final String name) {
        return declareListOperator(name,Object.class);
    }

    @Override
    public ExpressionManager declareListOperator(final String name, Class argType) {
        return declareListOperator(name, argType, argType, new FunctionHandler() {
            @Override
            public Object evaluate(FunctionEvaluationContext context) {
                ExpressionNode[] arguments = context.getArguments();
                if (arguments.length == 1) {
                    Object y = arguments[0].evaluate(context.getExpressionEvaluator());
                    if (y instanceof Object[]) {
                        return new ExpressionUplet(name, (Object[]) y);
                    } else {
                        return new ExpressionUplet(name, new Object[]{y});
                    }
                } else {
                    Object[] ll = new Object[arguments.length];
                    for (int i = 0; i < arguments.length; i++) {
                        ll[i] = arguments[i].evaluate(context.getExpressionEvaluator());
                    }
                    return new ExpressionUplet(name, ll);
                }
            }
        });
    }

    public static Class toArrayClass(Class argType) {
        try {
            return Class.forName("[L" + argType.getName() + ";");
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Should never happen");
        }
    }

    private static class ListOperator extends FunctionBase {

        private FunctionHandler operator;
        private Class operandTypeArr;

        public ListOperator(FunctionHandler operator, String name, Class resultType, Class operandType) {
//            super(name, resultType, true, toArrayClass(operandType));
            super(name, resultType, new Class[]{
                toArrayClass(operandType)
            }, true);
            this.operator = operator;
            this.operandTypeArr = toArrayClass(operandType);
        }

        @Override
        public Object eval(ExpressionNode[] operands, ExpressionEvaluator evaluator) {
            return operator.evaluate(new DefaultFunctionEvaluationContext(evaluator, evaluator.getExpressionManager(),operands, getName(), new Class[]{operandTypeArr},
                    getResultType(evaluator.getExpressionManager(), operands)));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String n = getName();
            if (n == null) {
                n = "<IMPLICIT>";
            }
            sb.append(n).append("(");
            sb.append(operandTypeArr.getComponentType().getSimpleName()).append("...");
            sb.append(")");
            return "ExpressionListOperator{" + "operator=" + operator + ", resultType=" + getResultType(null, null) + ", operandTypeArr=" + operandTypeArr + '}';
        }

    }

    @Override
    public ExpressionManager declareListOperator(String name, Class returnType, Class argType, FunctionHandler operator) {
        return declareOperator(new ListOperator(operator, name, returnType, argType));
    }

    public ExpressionManager declareOperator(Function fct) {
        functionsRepository.declareOperator(fct);
        invalidateFunctionsCache(fct.getSignature());
        return this;
    }

    public ExpressionManager declareOperatorAlias(String alias, String referenceOp, Class... operands) {
        functionsRepository.declareOperatorAlias(alias, referenceOp, operands, false);
        invalidateFunctionsCache(new MethodSignature(alias, operands, false));
        return this;
    }

    public ExpressionManager declareOperatorAlias(String alias, String referenceOp, boolean varArgs, Class... operands) {
        functionsRepository.declareOperatorAlias(alias, referenceOp, operands, varArgs);
        invalidateFunctionsCache(new MethodSignature(alias, operands, varArgs));
        return this;
    }

    public ExpressionManager undeclareOperator(String op, Class... operands) {
        functionsRepository.undeclareOperatorAlias(op, operands);
        functionsRepository.undeclareOperator(op, operands);
        invalidateFunctionsCache(new MethodSignature(op, operands, false));
        return this;
    }

    public boolean isReadOnlyVariable(String varName) {
        Variable ff = findVariable(getCanonicalName(varName));
        return ff != null && ff.isReadOnly();
    }

    @Override
    public void debug(String message) {
        //System.out.println("## DEBUG "+message);
    }


    public ExpressionNodeVariableName getVariableName(String varName) {
        Variable o = getVariable(varName);
//        return new ExpressionNodeVariableName(varName, o == null ? Object.class : o.getEffectiveType(this));
        return new ExpressionNodeVariableName(varName, o == null ? Object.class : o.getType());
    }

//    public Variable getVariable(String varName) {
//        Variable d = localVariables.get(varName);
//        if (d == null) {
//            d = definition.getVariable(varName);
//        }
//        return d;
//    }

    public ExpressionNode parse(String expression) {
        try {
            return new DefaultExpressionParser(expression, this).parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public Object evaluateFunction(String name, ExpressionNode... args) {
//        return evaluate(resolveFunctionCallNode(name, args));
//    }
    private Object invokeInContext(SilentCallable invocable) {
        if (ExpressionEvaluatorSharedContextHelper.getCurrent() == null) {
            return ExpressionEvaluatorSharedContextHelper.invokeSilentCallable(this, invocable);
        } else {
            return invocable.call();
        }
    }

//    public Object evaluate(ExpressionNode node) {
//        if (ExpressionEvaluatorSharedContextHelper.getCurrent() == null) {
//            return ExpressionEvaluatorSharedContextHelper.invokeSilentCallable(this, new SilentCallable<Object>() {
//                @Override
//                public Object call() {
//                    return node.evaluate(DefaultExpressionManager.this);
//                }
//            });
//        } else {
//            return node.evaluate(DefaultExpressionManager.this);
//        }
//    }

    public ExpressionEvaluator createEvaluator(String expression) {
        return new StringExpressionEvaluator(expression);
    }

    public ExpressionEvaluator createEvaluator(ExpressionNode expression) {
        return new ExpressionNodeExpressionEvaluator(expression);
    }

//    public Object evaluate(String expression) {
////        return ExpressionEvaluatorSharedContextHelper.invokeSilentCallable(this, new SilentCallable<Object>() {
////            @Override
////            public Object call() {
////                ExpressionNode o = parse(expression);
////                if (o == null) {
////                    return null;
////                }
////                return o.evaluate(DefaultExpressionManager.this);
////            }
////        });
//        createEvaluator()
//        ExpressionNode n = parse(expression);
//        return evaluate(n);
//
//    }

    private static class FunctionFromVariable implements Function {

        private final Variable v;

        public FunctionFromVariable(Variable v) {
            this.v = v;
        }

        @Override
        public Class getResultType(ExpressionManager evaluator, ExpressionNode[] args) {
            return v.getType();
        }

        @Override
        public Class getEffectiveResultType(ExpressionEvaluator evaluator, ExpressionNode[] args) {
            return v.getEffectiveType(evaluator);
        }

        @Override
        public boolean isVarArgs() {
            return false;
        }

        @Override
        public Class[] getArgTypes() {
            return new Class[0];
        }

        @Override
        public Object eval(ExpressionNode[] args, ExpressionEvaluator evaluator) {
            return v.getValue(evaluator);
        }

        @Override
        public String getName() {
            return v.getName();
        }

        @Override
        public MethodSignature getSignature() {
            return new MethodSignature(getName(),getArgTypes(),isVarArgs());
        }
    }

    private abstract class AbstractExpressionEvaluator implements ExpressionEvaluator {
        private Map<String,Object> userProperties=new HashMap<>();

        public void setVariableValue(String varName, Object value) {
            getVariable(varName).setValue(value, this);
        }

        public Object getVariableValue(String varName) {
            return getVariable(varName).getValue(this);
        }

        @Override
        public Map<String, Object> getUserProperties() {
            return userProperties;
        }

        @Override
        public Object evaluateFunction(String name, ExpressionNode... args) {
            Class[] argTypes = getTypes(args);
            try {
                if (argTypes == null /*|| argTypes.length == 0*/) {
                    ExpressionNode p = findVariableNode(name);
                    if (p != null) {
                        return p.evaluate(this);
                    }
                }
                if (argTypes == null) {
                    argTypes = new Class[0];
                }
                if (args == null) {
                    args = new ExpressionNode[0];
                }
                ExpressionNode[] finalArgs = args;
                Function op2 = functionsRepository.findFunction(name, args, this.getExpressionManager());
                if (op2 != null) {
                    return op2.eval(finalArgs, AbstractExpressionEvaluator.this);
                }
            } catch (Exception ex) {
                throw new RuntimeException(toStr(name, args) + " failed to execute", ex);
            }
            String opName = new MethodSignature(getCanonicalName(name), argTypes, false).toString();
            if (functionsRepository.containsOperator(getCanonicalName(name))) {
                throw new NoSuchElementException(toStr(name, args) + " failed to execute" + " . Operator " + opName + " is abstract. Unable to resolve any implementation of it.");
            }
            throw new NoSuchElementException(toStr(name, args) + " failed to execute" + ". " + opName + " not found");
        }

    }
    private class StringExpressionEvaluator extends AbstractExpressionEvaluator {
        private final String expression;

        public StringExpressionEvaluator(String expression) {
            this.expression = expression;
        }

        @Override
        public ExpressionManager getExpressionManager() {
            return DefaultExpressionManager.this;
        }

        @Override
        public Object evaluate() {
            ExpressionNode o = parse(expression);
            if (o == null) {
                return null;
            }
            return o.evaluate(this);
        }
    }
    private class ExpressionNodeExpressionEvaluator extends AbstractExpressionEvaluator {
        private final ExpressionNode expression;

        public ExpressionNodeExpressionEvaluator(ExpressionNode expression) {
            this.expression = expression;
        }

        @Override
        public ExpressionManager getExpressionManager() {
            return DefaultExpressionManager.this;
        }

        @Override
        public Object evaluate() {
            if (expression == null) {
                return null;
            }
            return expression.evaluate(this);
        }
    }
}
