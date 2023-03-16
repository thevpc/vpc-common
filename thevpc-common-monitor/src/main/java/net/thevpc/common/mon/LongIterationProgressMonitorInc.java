package net.thevpc.common.mon;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class LongIterationProgressMonitorInc implements ProgressMonitorInc {
    private double max;
    private long index;

    public LongIterationProgressMonitorInc(long max) {
        this.max = max;
    }

    @Override
    public double inc(double last) {
        long lastInt = (long) (last * max);
        return (lastInt + 1) / max;
    }
}
