package net.thevpc.jshell;


import java.io.InputStream;
import java.io.PrintStream;

public class DefaultJShellExecutionContext implements JShellExecutionContext {
    private final JShellContext context;

    public DefaultJShellExecutionContext(JShellContext context) {
        this.context = context;
    }

    @Override
    public JShell getShell() {
        return context.getShell();
    }

    @Override
    public PrintStream out() {
        return context.out();
    }

    @Override
    public PrintStream err() {
        return context.err();
    }

    @Override
    public JShellVariables vars() {
        return context.vars();
    }

    @Override
    public InputStream in() {
        return context.in();
    }

    @Override
    public JShellContext getGlobalContext() {
        return context;
    }
}
