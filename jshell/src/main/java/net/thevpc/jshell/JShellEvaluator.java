/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jshell;

import net.thevpc.jshell.parser2.InstructionNode;

/**
 *
 * @author thevpc
 */
public interface JShellEvaluator {

    void evalSuffixOperation(String opString, InstructionNode node, JShellFileContext context);

    void evalSuffixAndOperation(InstructionNode node, JShellFileContext context);

    void evalBinaryAndOperation(InstructionNode left, InstructionNode right, JShellFileContext context);

    void evalBinaryOperation(String opString, InstructionNode left, InstructionNode right, JShellFileContext context);

    void evalBinaryOrOperation(InstructionNode left, InstructionNode right, JShellFileContext context);

    void evalBinaryPipeOperation(final InstructionNode left, InstructionNode right, final JShellFileContext context);

    void evalBinarySuiteOperation(InstructionNode left, InstructionNode right, JShellFileContext context);

    String evalCommandAndReturnString(InstructionNode left, JShellFileContext context);


    String evalDollarSharp(JShellFileContext context);

    String evalDollarName(String name, JShellFileContext context);

    String evalDollarInterrogation(JShellFileContext context);

    String evalDollarInteger(int index, JShellFileContext context);

    String evalDollarExpression(String stringExpression, JShellFileContext context);

    String evalSimpleQuotesExpression(String expressionString, JShellFileContext context);

    String evalDoubleQuotesExpression(String stringExpression, JShellFileContext context);

    String evalAntiQuotesExpression(String stringExpression, JShellFileContext context);

    String evalNoQuotesExpression(String stringExpression, JShellFileContext context);

    String expandEnvVars(String stringExpression, boolean escapeResultPath, JShellFileContext context);

}
