package net.thevpc.common.mon;

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
    public double inc(double lastVal) {
        int lastInt = (int) (lastVal * max);
        double newVal=(lastInt + 1) / max;
        int newInt = (int) (newVal * max);
        return newVal;
    }

}
