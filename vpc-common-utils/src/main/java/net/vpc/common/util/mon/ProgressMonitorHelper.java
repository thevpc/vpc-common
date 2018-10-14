package net.vpc.common.util.mon;


import net.vpc.common.util.Chronometer;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class ProgressMonitorHelper {
    private ProgressMonitorInc incrementor = new DeltaProgressMonitorInc(1E-2);
    private Chronometer chronometer;
    private boolean paused = false;
    private boolean cancelled = false;
    private boolean started = false;

    public ProgressMonitorHelper() {
    }


    public void setIncrementor(ProgressMonitorInc incrementor) {
        this.incrementor = incrementor;
    }

    public ProgressMonitorInc getIncrementor() {
        return this.incrementor;
    }

    public void cancel() {
        cancelled = true;
    }

    public void resume() {
        paused = false;
    }

    public void suspend() {
        paused = true;
    }


    public boolean isCanceled() {
        return cancelled;
    }


    public Chronometer getChronometer() {
        if (chronometer == null) {
            chronometer = new Chronometer(false);
        }
        return chronometer;
    }


    public void stop() {

    }
}
