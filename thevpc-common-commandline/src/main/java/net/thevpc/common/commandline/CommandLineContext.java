package net.thevpc.common.commandline;

public interface CommandLineContext {
    String[] getArgs();
    CommandAutoComplete getAutoComplete();

}
