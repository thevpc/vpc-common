package net.vpc.common.mon;

import java.util.logging.Level;


public abstract class AbstractProgressMonitor extends AbstractTaskMonitor implements ProgressMonitor {

    private ProgressMonitorInc incrementor = new DeltaProgressMonitorInc(1E-2);
    private double progress=Double.NaN;

    public AbstractProgressMonitor(long id) {
        super(id);
    }

    @Override
    public double getProgressValue() {
        return progress;
    }


    public final void setProgress(double progress, TaskMessage message) {
        setProgress(progress);
        setMessage(message);
    }

    public ProgressMonitor[] split(int nbrElements) {
        return ProgressMonitors.split(this, nbrElements);
    }

    public ProgressMonitor[] split(double[] weight) {
        return ProgressMonitors.split(this, weight);
    }

    public ProgressMonitor[] split(double[] weight, boolean[] enabledElements) {
        return ProgressMonitors.split(this, weight, enabledElements);
    }

    public ProgressMonitor translate(double factor, double start) {
        return ProgressMonitors.translate(this, factor, start);
    }

    public ProgressMonitor translate(int index, int max) {
        return new ProgressMonitorTranslator(this, 1.0 / max, index * (1.0 / max));
    }

    public final void setProgress(double progress) {
        if (!isStarted()) {
            start();
        }
        if (isCanceled()) {
            return;
        }
        if (isTerminated()) {
            return;
        }
        if (isSuspended()) {
            while (isSuspended()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        }
        if ((progress < 0 || progress > 1) && !Double.isNaN(progress)) {
            if (ProgressMonitors.Config.isStrictComputationMonitor()) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress);
            } else {
                if (progress < 0) {
                    progress = 0;
                } else if (progress > 1) {
                    progress = 1;
                }
            }
        }
        if (ProgressMonitors.Config.isStrictComputationMonitor()) {
            if (!Double.isNaN(progress) && progress < getProgressValue() && getProgressValue() >= 1) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress + "<" + getProgressValue());
            }
        }
        if(this.progress != progress){
            this.progress = progress;
            setProgressImpl(progress);
        }
        if (progress >= 1) {
            if (!isTerminated()) {
                terminate();
            }
        }
    }

//    public void setProgress(double i) {
//        setProgress(i, getProgressMessage());
//    }

    public void setProgress(int i, int max) {
        this.setProgress((1.0 * i / max), getProgressMessage());
    }

    public void setProgress(int i, int max, String message) {
        this.setProgress((1.0 * i / max), message);
    }

    public void setProgress(int i, int max, String message, Object... args) {
        this.setProgress((1.0 * i / max), message, args);
    }

    public void setProgress(int i, int j, int maxi, int maxj, String message) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), message);
    }

    public void setProgress(int i, int j, int maxi, int maxj, String message, Object... args) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), message, args);
    }

    public void setProgress(double progress, String message) {
        setProgress(progress, new StringTaskMessage(Level.FINE, message));
    }

    public void setProgress(double progress, String message, Object... args) {
        setProgress(progress, new FormattedTaskMessage(Level.FINE, message, args));
    }

    public ProgressMonitor setIndeterminate(String message) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new StringTaskMessage(Level.FINE, message));
        return this;
    }

    public ProgressMonitor setIndeterminate(String message, Object... args) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new FormattedTaskMessage(Level.FINE, message, args));
        return this;
    }

    public ProgressMonitor incremental(int iterations) {
        return ProgressMonitors.incremental(this, iterations);
    }

    public ProgressMonitor incremental(ProgressMonitor baseMonitor, double delta) {
        return ProgressMonitors.incremental(this, delta);
    }

    public ProgressMonitor temporize(long freq) {
        return ProgressMonitors.temporize(this, freq);
    }

    public ProgressMonitor[] split(boolean... enabledElements) {
        return ProgressMonitors.split(this, enabledElements);
    }

    public ProgressMonitorInc getIncrementor() {
        return this.incrementor;
    }

    public ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) {
        this.incrementor = incrementor;
        return this;
    }

    public ProgressMonitor inc() {
        inc("");
        return this;
    }

    public ProgressMonitor inc(String message) {
        ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("Missing Incrementor");
        }
        setProgress(incrementor.inc(getProgressValue()), new StringTaskMessage(Level.FINE, message));
        return this;
    }

    public ProgressMonitor inc(String message, Object... args) {
        ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("Missing Incrementor");
        }
        setProgress(incrementor.inc(getProgressValue()), new FormattedTaskMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public long getEstimatedTotalDuration() {
        double d = getProgressValue();
        long spent = getDuration();
        if (spent <= 0) {
            return -1;
        }
        return (long) ((spent / d));
    }

    @Override
    public long getEstimatedRemainingDuration() {
        double d = getProgressValue();
        long spent = getDuration();
        if (spent <= 0) {
            return -1;
        }
        return (long) ((spent / d) * (1 - d));
    }


    public ProgressMonitor terminate(String message) {
        if (!isTerminated()) {
            super.terminate();
            setProgress(1, new StringTaskMessage(Level.INFO, message));
        }
        return this;
    }

    public ProgressMonitor terminate(String message, Object... args) {
        if (!isTerminated()) {
            setProgress(1, new FormattedTaskMessage(Level.INFO, message, args));
        }
        return this;
    }

    public ProgressMonitor start(String message) {
        if (!isStarted()) {
            setProgress(0, new StringTaskMessage(Level.FINE, message));
        }
        return this;
    }

    public ProgressMonitor start(String message, Object... args) {
        if (!isStarted()) {
            start();
            setProgress(0, new FormattedTaskMessage(Level.INFO, message, args));
        }
        return this;
    }

    @Override
    protected void startImpl() {
        setProgress(0, new StringTaskMessage(Level.FINE, ""));
    }

    protected void terminateImpl() {
        setProgress(1, new StringTaskMessage(Level.FINE, ""));
    }

    protected void setProgressImpl(double progress) {

    }

}

