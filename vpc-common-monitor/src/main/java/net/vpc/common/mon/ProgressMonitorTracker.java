package net.vpc.common.mon;

public abstract class ProgressMonitorTracker extends BaseProgressMonitor {
    private double value;
    private ProgressMessage message;

    @Override
    protected void setProgressImpl(double progress, ProgressMessage message) {
        this.value=progress;
        this.message=message;
        onProgress(progress,message);
    }

    protected abstract void onProgress(double progress, ProgressMessage message) ;

    @Override
    public double getProgressValue() {
        return value;
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }
}
