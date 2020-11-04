/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jshell;

import net.thevpc.jshell.parser.nodes.InstructionNode;

/**
 *
 * @author vpc
 */
public interface JShellNodeEvaluator {

    void evalBinaryAndOperation(InstructionNode left, InstructionNode right, JShellContext context);

    void evalBinaryOperation(String opString, InstructionNode left, InstructionNode right, JShellContext context);

    void evalBinaryOrOperation(InstructionNode left, InstructionNode right, JShellContext context);

    void evalBinaryPipeOperation(final InstructionNode left, InstructionNode right, final JShellContext context);

    void evalBinarySuiteOperation(InstructionNode left, InstructionNode right, JShellContext context);
    
}
