package net.vpc.common.jeep.nodes;

public class BreakException extends RuntimeException{
    private String name;

    public BreakException() {
        this(null);
    }

    public BreakException(String name) {
        super("Break to "+(name==null?"<ANY>":"name"));
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
