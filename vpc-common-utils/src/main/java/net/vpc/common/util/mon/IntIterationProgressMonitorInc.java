package net.vpc.common.util.mon;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class IntIterationProgressMonitorInc implements ProgressMonitorInc {
    private double max;
    private int index;

    public IntIterationProgressMonitorInc(int max) {
        this.max = max;
    }

    @Override
    public double inc(double last) {
        index++;
        return index/max;
    }
}
