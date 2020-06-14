/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.functions.JMultipleInvokableMatchFound;
import net.vpc.common.jeep.impl.functions.JNameSignature;
import net.vpc.common.jeep.impl.functions.JSignature;

import java.util.function.Function;

/**
 * @author vpc
 */
public interface JFunctions {

    void declare(JFunction JFunction);

    void declare(String name, String[] args, String returnType, JInvoke function);

    JFunction declare(JSignature signature, JType returnType, JInvoke function);

    JFunction declare(String name, JType[] args, JType returnType, boolean varArgs, JInvoke function);

    //    @Override
    Object evaluate(String name, JEvaluable... args);

    void declareAlias(String alias, String referenceOp, boolean varArgs, JType... operands);

    void removeFunction(JNameSignature signature);

    void removeFunction(JSignature signature);

    JFunction[] findFunctions(String name, int callArgumentsCount);

    JFunction findFunctionMatchOrNull(JSignature sig);

    JFunction findFunctionMatch(JSignature sig);

    void undeclareAlias(String alias, String[] operands);

    void undeclareAlias(String alias);

    JFunction findFunctionMatchOrNull(JNameSignature signature);

    JFunction findFunctionMatchOrNull(String name);

    JFunction findFunctionExact(JNameSignature signature);

    JFunction findFunctionExact(JSignature signature);

    JFunction findFunctionExactOrNull(JSignature signature);

    JFunction[] findFunctionsByName(String name);


    JInvokable resolveBestMatch(JInvokable[] invokables, Function<JTypeOrLambda, JConverter[]> convertersSupplier, JTypeOrLambda... argTypes) throws JMultipleInvokableMatchFound;

    JInvokableCost[] resolveMatches(boolean bestMatchOnly, JInvokable[] invokables, Function<JTypeOrLambda, JConverter[]> convertersSupplier, JTypeOrLambda... argTypes);
}
