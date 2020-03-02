package net.vpc.common.mon;

public class ProgressMonitorDelegate extends TaskMonitorDelegate implements ProgressMonitor {
    public ProgressMonitorDelegate(TaskMonitor delegate) {
        super(delegate);
    }

    protected ProgressMonitor getDelegate() {
        return (ProgressMonitor) super.getDelegate();
    }

    @Override
    public double getProgressValue() {
        return getDelegate().getProgressValue();
    }



    @Override
    public void setProgress(double progress, TaskMessage message) {
        getDelegate().setProgress(progress, message);
    }

    @Override
    public ProgressMonitor[] split(int nbrElements) {
        return getDelegate().split(nbrElements);
    }

    @Override
    public ProgressMonitor[] split(double[] weight) {
        return getDelegate().split(weight);
    }

    @Override
    public ProgressMonitor[] split(double[] weight, boolean[] enabledElements) {
        return getDelegate().split(weight, enabledElements);
    }

    @Override
    public ProgressMonitor translate(double factor, double start) {
        return getDelegate().translate(factor, start);
    }

    @Override
    public ProgressMonitor translate(int index, int max) {
        return getDelegate().translate(index, max);
    }

    @Override
    public void setProgress(double i) {
        getDelegate().setProgress(i);
    }

    @Override
    public void setProgress(int i, int max) {
        getDelegate().setProgress(i, max);
    }

    @Override
    public void setProgress(int i, int max, String message) {
        getDelegate().setProgress(i, max, message);
    }

    @Override
    public void setProgress(int i, int max, String message, Object... args) {
        getDelegate().setProgress(i, max, message, args);
    }

    @Override
    public void setProgress(int i, int j, int maxi, int maxj, String message) {
        getDelegate().setProgress(i, j, maxi, maxj, message);
    }

    @Override
    public void setProgress(int i, int j, int maxi, int maxj, String message, Object... args) {
        getDelegate().setProgress(i, j, maxi, maxj, message, args);
    }

    @Override
    public void setProgress(double progress, String message) {
        getDelegate().setProgress(progress, message);
    }

    @Override
    public void setProgress(double progress, String message, Object... args) {
        getDelegate().setProgress(progress, message, args);
    }


    @Override
    public ProgressMonitor setIndeterminate(String message) {
        return getDelegate().setIndeterminate(message);
    }


    @Override
    public ProgressMonitor setIndeterminate(String message, Object... args) {
        return getDelegate().setIndeterminate(message, args);
    }


    @Override
    public ProgressMonitor incremental(int iterations) {
        return getDelegate().incremental(iterations);
    }

    @Override
    public ProgressMonitor incremental(ProgressMonitor baseMonitor, double delta) {
        return getDelegate().incremental(baseMonitor, delta);
    }

    @Override
    public ProgressMonitor temporize(long freq) {
        return getDelegate().temporize(freq);
    }

    @Override
    public ProgressMonitor[] split(boolean... enabledElements) {
        return getDelegate().split(enabledElements);
    }

    @Override
    public ProgressMonitorInc getIncrementor() {
        return getDelegate().getIncrementor();
    }

    @Override
    public ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) {
        return getDelegate().setIncrementor(incrementor);
    }

    @Override
    public ProgressMonitor inc() {
        return getDelegate().inc();
    }

    @Override
    public ProgressMonitor inc(String message) {
        return getDelegate().inc(message);
    }

    @Override
    public ProgressMonitor inc(String message, Object... args) {
        return getDelegate().inc(message, args);
    }

    @Override
    public long getEstimatedTotalDuration() {
        return getDelegate().getEstimatedTotalDuration();
    }

    @Override
    public long getEstimatedRemainingDuration() {
        return getDelegate().getEstimatedRemainingDuration();
    }


    @Override
    public ProgressMonitor terminate(String message) {
        return getDelegate().terminate(message);
    }

    @Override
    public ProgressMonitor terminate(String message, Object... args) {
        return getDelegate().terminate(message, args);
    }

    @Override
    public ProgressMonitor start(String message) {
        return getDelegate().start(message);
    }

    @Override
    public ProgressMonitor start(String message, Object... args) {
        return getDelegate().start(message, args);
    }
}
