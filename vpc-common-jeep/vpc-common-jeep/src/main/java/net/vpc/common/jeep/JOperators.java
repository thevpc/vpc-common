/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 * @author vpc
 */
public interface JOperators {

    JOperators undeclareOperator(JOperator operator);

    JOperators declareOperator(JOperator operator, int precedence);

    JOperators declareBinaryOperator(String operator);

    JOperators declareBinaryOperator(String operator, int precedence);

    JOperators declareBinaryOperators(int precedence,String... operator);
    JOperators declareBinaryOperators(String... operator);

    JOperators declareListOperator(final String name);

    JOperators declareListOperator(final String name, Class argType);

    JOperators declareListOperator(final String name, String argType);

    JOperators declareListOperator(final String name, JType argType);

    boolean isOperator(JOperator operator);

    int getOperatorPrecedence(JOperator operator);

    boolean isListSeparator(String op);

    JOperators undeclareOperator(String op, JType... operands);

    JOperators declareOperatorAlias(String alias, String referenceOp, boolean varArgs, JType... operands);

    JOperators declareOperator(JFunction fct);

    JOperators declareListOperator(String name, String returnType, String argType, JInvoke operator);

    JOperators declareListOperator(String name, Class returnType, Class argType, JInvoke operator);

    JOperators declareListOperator(String name, JType returnType, JType argType, JInvoke operator);

    boolean isPrefixUnaryOperator(String op);

    JOperators declareCStyleOperators();

    JOperators declarePrefixUnaryOperator(String operator);

    JOperators declarePrefixUnaryOperator(String operator, int precedence);

    JOperators declarePrefixUnaryOperators(String... operator);
    
    JOperators declarePrefixUnaryOperators(int precedence,String... operator);

    JOperators undeclarePrefixUnaryOperator(String operator);

    JOperators undeclarePrefixUnaryOperators(String... operators);

    boolean isPostfixUnaryOperator(String op);

    JOperators declarePostfixUnaryOperator(String operator);

    JOperators declarePostfixUnaryOperator(String operator, int precedence);

    JOperators declareSpecialOperator(String operator);

    JOperators declareSpecialOperators(String... operators);

    JOperators declarePostfixUnaryOperators(String... operator);

    JOperators undeclarePostfixUnaryOperator(String operator);

    JOperators undeclarePostfixUnaryOperators(String... operators);

    boolean isImplicitOperatorEnabled();

    boolean isOperator(String operator);

    void undeclareOperatorAlias(String alias, JType[] operands);

    boolean isBinaryOperator(String operator);

    JOperators declarePostfixUnaryOperators(int precedence, String ...operators);
}