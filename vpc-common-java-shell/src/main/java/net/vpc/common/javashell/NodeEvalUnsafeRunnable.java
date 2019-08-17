/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.javashell;

import net.vpc.common.javashell.parser.nodes.InstructionNode;

/**
 *
 * @author vpc
 */
public class NodeEvalUnsafeRunnable implements UnsafeRunnable {
    
    private final InstructionNode left;
    private final JShellContext context;

    public NodeEvalUnsafeRunnable(InstructionNode left, JShellContext context) {
        this.left = left;
        this.context = context;
    }

    @Override
    public void run() throws Exception {
        left.eval(context);
    }
    
}
