package net.thevpc.jshell.parser2;

import net.thevpc.jshell.JShellFileContext;

public interface Command extends InstructionNode, Yaccer.Node2 {
    void eval(JShellFileContext context);

}
