/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import net.vpc.common.jeep.nodes.ExpressionNodeVariableName;

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
public interface ExpressionManager {

    ExpressionManager configureDefaults();

    void debug(String message);

    ExpressionNode parse(String expression);

    ExpressionEvaluator createEvaluator(String expression);

    ExpressionEvaluator createEvaluator(ExpressionNode node);

    ExpressionNodeVariableName getVariableName(String varName);

    ExpressionStreamTokenizerConfig getTokenizerConfig();

    ExpressionEvaluatorResolver[] getResolvers();

    ExpressionManager declareBinaryOperator(String operator);

    ExpressionManager declareBinaryOperator(String operator, long precedence);

    ExpressionManager declareBinaryOperators(String... operator);

    Variable declareConst(String name, Class type, Object value);

    Variable declareConst(String name, Object value);

    void declareFunction(Function function);

    void declareFunction(String name, Class[] args, Class returnType, boolean varArgs, FunctionHandler function);

    ExpressionManager declareListOperator(String name);

    ExpressionManager declareListOperator(String name, Class argType);

    ExpressionManager declareListOperator(String name, Class returnType, Class argType, FunctionHandler operator);

    ExpressionManager declareOperator(Function operator);

    ExpressionManager declareOperatorAlias(String alias, String referenceOp, Class... operands);

    ExpressionManager declareUnaryOperator(String operator);

    ExpressionManager declareUnaryOperator(String operator, long precedence);

    ExpressionManager importType(Class type);

    ExpressionManager importMethods(Class type);

    ExpressionManager importFields(Class type);

    ExpressionManager addResolver(ExpressionEvaluatorResolver resolver);

    ExpressionManager removeResolver(ExpressionEvaluatorResolver resolver);

    ExpressionManager declareUnaryOperators(String... operator);

    Variable declareVar(String name, Class type, Class undefinedType, Object value);

    void declareVar(Variable def);

    Variable declareVar(String name, Class type, Object value);

    void setTokenizerConfig(ExpressionStreamTokenizerConfig tokenizerConfig);

    boolean isCaseInsensitive();

    ExpressionManager declareVarAlias(String alias, String name);

    boolean isBinaryOperator(String op);

    boolean isUnaryOperator(String op);

    ExpressionManager undeclareOperator(String op, Class... operands);

    void undeclareVar(String name);

    ExpressionManager undeclareVarAlias(String alias);

    long getOperatorPrecedence(String op);

    boolean isListSeparator(String op);

    Function findFunction(String name, ExpressionNode... args);

    Variable findVariable(String var);

    Variable getVariable(String var);

    Class[] getExprTypes(ExpressionNode[] nodes);
}
