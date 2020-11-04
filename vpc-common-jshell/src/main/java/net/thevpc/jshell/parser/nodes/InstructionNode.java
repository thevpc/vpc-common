/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jshell.parser.nodes;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.thevpc.jshell.JShellContext;

/**
 * @author vpc
 */
public abstract class InstructionNode implements Node {

    public abstract void eval(JShellContext context);

    // will ignore
    public String evalString(JShellContext context) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JShellContext c2 = context.getShell().createContext(context);
        c2.setOut(new PrintStream(out));
        eval(c2);
        c2.out().flush();
        return out.toString();
    }
}
