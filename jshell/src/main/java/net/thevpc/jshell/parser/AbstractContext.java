package net.thevpc.jshell.parser;

public abstract class AbstractContext implements Context {
    protected final JShellParser reader;

    public AbstractContext(JShellParser reader) {
        this.reader = reader;
    }

}
