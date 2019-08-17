package net.vpc.common.mvn;

public interface PomIdFilter {
    boolean accept(PomId id);
}
