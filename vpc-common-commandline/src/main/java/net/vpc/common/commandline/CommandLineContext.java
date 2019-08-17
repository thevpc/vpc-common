package net.vpc.common.commandline;

public interface CommandLineContext {
    String[] getArgs();
    CommandAutoComplete getAutoComplete();

}
