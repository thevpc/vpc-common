package net.vpc.common.util.mon;

import net.vpc.common.util.Chronometer;

public class ReusableProgressMonitor implements ProgressMonitor {
    private final ProgressMonitor base;

    public ReusableProgressMonitor(ProgressMonitor base) {
        this.base = base;
    }

    @Override
    public double getProgressValue() {
        return base.getProgressValue();
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return base.getProgressMessage();
    }

    @Override
    public void setProgress(double progress, ProgressMessage message) {
        if (progress == 1.0) {
            progress = Double.NaN;
        }
        base.setProgress(progress, message);
    }

    @Override
    public boolean isCanceled() {
        return base.isCanceled();
    }

    @Override
    public void stop() {
        base.stop();
    }

    public void terminateAll() {
        base.terminate("");
    }

    @Override
    public ProgressMonitorInc getIncrementor() {
        return base.getIncrementor();
    }

    @Override
    public ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) {
        base.setIncrementor(incrementor);
        return this;
    }

    @Override
    public ProgressMonitor cancel() {
        base.cancel();
        return this;
    }

    @Override
    public ProgressMonitor resume() {
        base.resume();
        return this;
    }

    @Override
    public ProgressMonitor suspend() {
        base.suspend();
        return this;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public Chronometer getChronometer() {
        return base.getChronometer();
    }
}
