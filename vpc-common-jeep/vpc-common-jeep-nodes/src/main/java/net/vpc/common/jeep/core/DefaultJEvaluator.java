package net.vpc.common.jeep.core;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.nodes.*;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.JTypeArray;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class DefaultJEvaluator implements JEvaluator {
    private JContext jcontext;
    public DefaultJEvaluator(JContext jcontext) {
        this.jcontext=jcontext;
    }

    @Override
    public Object evaluate(JNode node, JInvokeContext context) {
        JDefaultNode dn=(JDefaultNode) node;
        switch (dn.id()) {
            case JNodeDefaultIds.NODE_ARRAY: {
                JNodeArray a = (JNodeArray) node;
                JTypes types = context.getContext().types();
                JNode[] values = a.getValues();
                JTypeArray jType = (JTypeArray) JNodeUtils2.getType(a);
                Object arr0 = jType.newArray(values.length);
                JArray arr = jType.asArray(arr0);
                int len = arr.length();
                for (int i = 0; i < len; i++) {
                    arr.set(i, evaluate(values[i], context));
                }
                return arr;
            }
            case JNodeDefaultIds.NODE_CONVERTER: {
                JNodeConverter a = (JNodeConverter) node;
                return a.getCurrentConverter().convert(evaluate(a.getNode(), context), context);
            }
            case JNodeDefaultIds.NODE_LITERAL: {
                JNodeLiteral a = (JNodeLiteral) node;
                return a.getValue();
            }
            case JNodeDefaultIds.NODE_ASSIGN: {
                JNodeAssign a = (JNodeAssign) node;
                JNode n = a.getName();
                JNode v = a.getValue();
                if (n instanceof JNodeVarName) {
                    context.getContext().vars().setValue(((JNodeVarName) n).getName(), evaluate(v, context), context);
                } else {
                    throw new IllegalArgumentException("Not Supported Yet");
                }
                return v;
            }
            case JNodeDefaultIds.NODE_BLOCK: {
                JNodeBlock a = (JNodeBlock) node;
                Object val = null;
                for (JNode statement : a.getStatements()) {
                    val = evaluate(statement, context);
                }
                return val;
            }
            case JNodeDefaultIds.NODE_BRACES: {
                JNodeBraces a = (JNodeBraces) node;
                return evaluateFunction(context,"{", a.getItems());
            }
            case JNodeDefaultIds.NODE_BRACKETS: {
                JNodeBrackets a = (JNodeBrackets) node;
                return evaluateFunction(context,"[", a.getItems());
            }
            case JNodeDefaultIds.NODE_PARS: {
                JNodePars a = (JNodePars) node;
                return evaluateFunction(context,"(", a.getItems());
            }
            case JNodeDefaultIds.NODE_BREAK: {
                JNodeBreak a = (JNodeBreak) node;
                return evaluateFunction(context,"break");
            }
            case JNodeDefaultIds.NODE_DECLARE_CLASS: {
                throw new IllegalArgumentException("Unsupporte declare class");
            }
            case JNodeDefaultIds.NODE_DECLARE_FUNCTION: {
                throw new IllegalArgumentException("Unsupported declare function");
            }
            case JNodeDefaultIds.NODE_DECLARE_VAR: {
                JNodeDeclareVar a = (JNodeDeclareVar) node;
                JNode name = a.getName();
                JType type = JNodeUtils2.getType(a);
                JNode value = a.getValue();
                Object newValue = value == null ? null : evaluate(value, context);
                context.getContext().vars().declareVar(((JNodeVarName) name).getName(),
                        type,
                        newValue
                );
                return newValue;
            }
            case JNodeDefaultIds.NODE_VAR_NAME: {
                JNodeVarName a = (JNodeVarName) node;
                return context.getContext().vars().getValue(a.getName(), context);
            }
            case JNodeDefaultIds.NODE_FUNCTION_CALL: {
                JNodeFunctionCall a = (JNodeFunctionCall) node;
                String name = a.getName();
                JNode[] nargs = a.getArgs();
                JType[] ntypes = JNodeUtils2.getTypes((JDefaultNode[]) nargs);
                JEvaluable[] eargs = JNodeUtils2.getEvaluatables((JDefaultNode[])nargs);
                JFunction f = context.getContext().functions().findFunctionMatchOrNull(JSignature.of(name, ntypes), context.getCallerInfo());
                if (f == null) {
                    JSignature sig = new JSignature(name, ntypes, false);
                    if (name.isEmpty()) {
                        throw new NoSuchElementException("Implicit JFunction not found " + sig + " for " + Arrays.asList(nargs));
                    }
                    throw new NoSuchElementException("JFunction not found " + sig + " for " + Arrays.asList(nargs));
//            return Object.class;
                }
                return f.invoke(
                        context.builder()
                                .setName(a.getName())
                                .setArguments(eargs)
                                .build()
                );
            }
            case JNodeDefaultIds.NODE_OP_BINARY_INFIX: {
                JNodeInfixBinaryOperatorCall c = (JNodeInfixBinaryOperatorCall) node;
                JDefaultNode[] nargs = {c.getArg1(), c.getArg2()};
                JType[] ntypes = JNodeUtils2.getTypes(nargs);
                JEvaluable[] eargs = JNodeUtils2.getEvaluatables((JDefaultNode[])nargs);
                JSignature sig = JSignature.of(c.getName(), ntypes);
                JFunction f = context.getContext().functions().findFunctionMatch(sig, context.getCallerInfo());
                return f.invoke(
                        context.builder()
                                .setName(sig.name())
                                .setArguments(eargs)
                                .build()
                );
            }
            case JNodeDefaultIds.NODE_OP_UNARY_PREFIX: {
                JNodePrefixUnaryOpCall c = (JNodePrefixUnaryOpCall) node;
                JNode[] nargs = {c.getArg()};
                JType[] ntypes = JNodeUtils2.getTypes((JDefaultNode[]) nargs);
                JEvaluable[] eargs = JNodeUtils2.getEvaluatables((JDefaultNode[])nargs);
                JSignature sig = JSignature.of(c.getName(), ntypes);
                JFunction f = context.getContext().functions().findFunctionMatchOrNull(sig, context.getCallerInfo());
                if (f == null) {
                    throw new NoSuchElementException("JFunction not found " + sig + " for " + Arrays.asList(nargs));
                }
                return f.invoke(
                        context.builder()
                                .setName(sig.name())
                                .setArguments(eargs)
                                .build()
                );
            }
            case JNodeDefaultIds.NODE_OP_UNARY: {
                JNodePostfixUnaryOpCall c = (JNodePostfixUnaryOpCall) node;
                JNode[] nargs = {c.getArg()};
                JType[] ntypes = JNodeUtils2.getTypes((JDefaultNode[]) nargs);
                JEvaluable[] eargs = JNodeUtils2.getEvaluatables((JDefaultNode[])nargs);
                JSignature sig = JSignature.of(c.getName(), ntypes);
                JFunction f = context.getContext().functions().findFunctionMatchOrNull(sig, context.getCallerInfo());
                if (f == null) {
                    throw new NoSuchElementException("JFunction not found " + sig + " for " + Arrays.asList(nargs));
                }
                return f.invoke(context.builder()
                        .setName(sig.name())
                        .setArguments(eargs)
                        .build());
            }
        }
        throw new IllegalArgumentException("Unsupported evaluation of node " + dn.id() + "@" + node.getClass().getSimpleName() + " : " + node);
    }

    private Object evaluateFunction(JInvokeContext context,String name,JNode... args){
        JType[] argTypes = JNodeUtils2.getTypes((JDefaultNode[]) args);
        JFunction f = context.getContext().functions().findFunctionMatch(JSignature.of(name, argTypes), context.getCallerInfo());
        return f.invoke(context.builder().setArguments(JNodeUtils2.getEvaluatables((JDefaultNode[])args)).setName(name).build());
    }


}
