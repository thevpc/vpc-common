package net.thevpc.jshell.parser2;

public abstract class AbstractContext implements Context {
    protected final JShellParser2 reader;

    public AbstractContext(JShellParser2 reader) {
        this.reader = reader;
    }

}
