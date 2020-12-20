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
public class NodeEvalUnsafeRunnable implements UnsafeRunnable {
    
    private final InstructionNode left;
    private final JShellFileContext context;

    public NodeEvalUnsafeRunnable(InstructionNode left, JShellFileContext context) {
        this.left = left;
        this.context = context;
    }

    @Override
    public void run() throws Exception {
        left.eval(context);
    }
    
}
