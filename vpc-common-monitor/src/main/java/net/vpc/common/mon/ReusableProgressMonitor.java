package net.vpc.common.mon;

public class ReusableProgressMonitor extends ProgressMonitorDelegate {

    public ReusableProgressMonitor(ProgressMonitor base) {
        super(base);
    }

    public void setProgress(double progress, TaskMessage message) {
        if (progress >= 1.0) {
            progress = Double.NaN;
        }
        super.setProgress(progress, message);
    }

    @Override
    public boolean isTerminated() {
        return false;
    }
}
