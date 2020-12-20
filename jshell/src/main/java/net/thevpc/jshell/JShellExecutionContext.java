package net.thevpc.jshell;

import java.io.InputStream;
import java.io.PrintStream;

public interface JShellExecutionContext {

    JShell getShell();

    InputStream in();

    PrintStream out();

    PrintStream err();

    JShellVariables vars();

    JShellFileContext getGlobalContext();
}
