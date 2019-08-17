package net.vpc.common.javashell;

public class JShellOptions {
    public boolean verbose = false;
    public boolean xtrace = false;

    public boolean isVerbose() {
        return verbose;
    }

    public JShellOptions setVerbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public boolean isXtrace() {
        return xtrace;
    }

    public JShellOptions setXtrace(boolean xtrace) {
        this.xtrace = xtrace;
        return this;
    }
}
