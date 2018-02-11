package net.vpc.common.util.mon;

/**
 * Created by vpc on 5/31/17.
 */
public abstract class AbstractProgressMonitor implements ProgressMonitor {
    double progress;
    ProgressMessage progressMessage;

    public double getProgressValue() {
        return progress;
    }

    public void setProgress(double progress, ProgressMessage message) {
        this.progress = progress;
        this.progressMessage = message;
        onProgress(progress, message);
    }

    protected abstract void onProgress(double progress, ProgressMessage message);

    @Override
    public ProgressMessage getProgressMessage() {
        return progressMessage;
    }

    @Override
    public String toString() {
        return "Monitor(" +
                getProgressValue() + ")";

    }
}