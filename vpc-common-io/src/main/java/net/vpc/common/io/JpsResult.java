package net.vpc.common.io;

public class JpsResult {
    private String pid;
    private String className;
    private String argsLine;

    public JpsResult(String pid, String className, String argsLine) {
        this.pid = pid;
        this.className = className;
        this.argsLine = argsLine;
    }

    public String getPid() {
        return pid;
    }

    public String getClassName() {
        return className;
    }

    public String getArgsLine() {
        return argsLine;
    }
}
