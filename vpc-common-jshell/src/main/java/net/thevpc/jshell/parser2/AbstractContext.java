package net.thevpc.jshell.parser2;

import net.thevpc.jshell.parser2.Context;
import net.thevpc.jshell.parser2.StringReader2;
import net.thevpc.jshell.parser2.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContext implements Context {
    protected final StringReader2 reader;

    public AbstractContext(StringReader2 reader) {
        this.reader = reader;
    }

}
