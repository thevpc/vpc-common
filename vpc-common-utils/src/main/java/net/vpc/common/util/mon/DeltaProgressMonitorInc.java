package net.vpc.common.util.mon;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class DeltaProgressMonitorInc implements ProgressMonitorInc {
    private double delta;

    public DeltaProgressMonitorInc(double delta) {
        this.delta = delta;
    }

    @Override
    public double inc(double last) {
        return last+delta;
    }
}
