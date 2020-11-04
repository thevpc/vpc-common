package net.thevpc.common.mon;


/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class ProgressMonitorHelper {
    private ProgressMonitorInc incrementor = new DeltaProgressMonitorInc(1E-2);
    private long startTime;
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


    public void stop() {

    }
}
