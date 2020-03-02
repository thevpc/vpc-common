package net.vpc.common.mon;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class SilentProgressMonitor extends AbstractProgressMonitor {

    public SilentProgressMonitor() {
        super(nextId());
    }

    @Override
    public String toString() {
        return "Silent(" +
                "value=" + getProgressValue() +
                ')';
    }
}
