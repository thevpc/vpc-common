package net.vpc.common.util.mon;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class SilentProgressMonitor extends AbstractProgressMonitor {
    private double progress;
    private ProgressMessage message;

    @Override
    public double getProgressValue() {
        return progress;
    }

    @Override
    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress = progress;
        this.message = message;
        //do nothing
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Silent(" +
                "value=" + getProgressValue() +
                ')';
    }
}
