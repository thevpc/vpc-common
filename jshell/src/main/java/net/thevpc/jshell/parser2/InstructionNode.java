package net.thevpc.jshell.parser2;

import net.thevpc.jshell.JShellFileContext;

public interface InstructionNode extends Node{
    void eval(final JShellFileContext context);
}
