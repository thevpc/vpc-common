package net.vpc.common.mon;

import java.util.logging.Level;

public abstract class AbstractProgressMonitor implements ProgressMonitor {
    private long startTime;
    private long stopTime;
    private boolean paused = false;
    private boolean cancelled = false;
    private boolean started = false;
    private ProgressMonitorInc incrementor = new DeltaProgressMonitorInc(1E-2);

    public ProgressMonitor[] split(int nbrElements) {
        return ProgressMonitorFactory.split(this, nbrElements);
    }

    public ProgressMonitor[] split(double[] weight) {
        return ProgressMonitorFactory.split(this, weight);
    }


    public ProgressMonitor[] split(double[] weight, boolean[] enabledElements) {
        return ProgressMonitorFactory.split(this, weight, enabledElements);
    }

    public ProgressMonitor translate(double factor, double start) {
        return ProgressMonitorFactory.translate(this, factor, start);
    }

    public ProgressMonitor translate(int index, int max) {
        return new ProgressMonitorTranslator(this, 1.0 / max, index * (1.0 / max));
    }

    public void setProgress(double i) {
        setProgress(i, getProgressMessage());
    }

    public void setProgress(int i, int max) {
        this.setProgress((1.0 * i / max), getProgressMessage());
    }

    public void setProgress(int i, int max, String message) {
        this.setProgress((1.0 * i / max), new StringProgressMessage(Level.FINE, message));
    }

    public void setProgress(int i, int max, String message, Object... args) {
        this.setProgress((1.0 * i / max), new FormattedProgressMessage(Level.FINE, message, args));
    }

    public void setProgress(int i, int j, int maxi, int maxj, String message) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new StringProgressMessage(Level.FINE, message));
    }

    public void setProgress(int i, int j, int maxi, int maxj, String message, Object... args) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new FormattedProgressMessage(Level.FINE, message, args));
    }

    public void setProgress(double progress, String message) {
        setProgress(progress, new StringProgressMessage(Level.FINE, message));
    }

    public void setProgress(double progress, String message, Object... args) {
        setProgress(progress, new FormattedProgressMessage(Level.FINE, message, args));
    }

    public ProgressMonitor setMessage(ProgressMessage message) {
        setProgress(getProgressValue(), message);
        return this;
    }

    public ProgressMonitor setIndeterminate(String message) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new StringProgressMessage(Level.FINE, message));
        return this;
    }

    public ProgressMonitor setMessage(String message, Object... args) {
        setProgress(getProgressValue(), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    public ProgressMonitor setIndeterminate(String message, Object... args) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }


    public ProgressMonitor setMessage(String message) {
        setProgress(getProgressValue(), new StringProgressMessage(Level.FINE, message));
        return this;
    }

    public ProgressMonitor start(String message) {
        setProgress(0, new StringProgressMessage(Level.FINE, message));
        return this;
    }

    public ProgressMonitor start(String message, Object... args) {
        setProgress(0, new FormattedProgressMessage(Level.INFO, message, args));
        return this;
    }

    public ProgressMonitor terminate(String message) {
        if (!isCanceled()) {
            setProgress(1, new StringProgressMessage(Level.INFO, message));
        }
        return this;
    }


    public ProgressMonitor terminate(String message, Object... args) {
        if (!isCanceled()) {
            setProgress(1, new FormattedProgressMessage(Level.INFO, message, args));
        }
        return this;
    }

    public ProgressMonitor createIncrementalMonitor(int iterations) {
        return ProgressMonitorFactory.createIncrementalMonitor(this, iterations);
    }

    public ProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, double delta) {
        return ProgressMonitorFactory.createIncrementalMonitor(this, delta);
    }

    public ProgressMonitor temporize(long freq) {
        return ProgressMonitorFactory.temporize(this, freq);
    }

    public ProgressMonitor[] split(boolean... enabledElements) {
        return ProgressMonitorFactory.split(this, enabledElements);
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
        setProgress(incrementor.inc(getProgressValue()), new StringProgressMessage(Level.FINE, message));
        return this;
    }


    public ProgressMonitor inc(String message, Object... args) {
        ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("Missing Incrementor");
        }
        setProgress(incrementor.inc(getProgressValue()), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }


    @Override
    public ProgressMonitor cancel() {
        cancelled = true;
        return this;
    }

    @Override
    public ProgressMonitor resume() {
        paused = false;
        return this;
    }

    @Override
    public ProgressMonitor suspend() {
        paused = true;
        return this;
    }

    @Override
    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }


    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        if (startTime == 0) {
            return -1;
        }
        if (stopTime == 0) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }


    public boolean isTerminated() {
        return getProgressValue() >= 1;
    }

    public final void setProgress(double progress, ProgressMessage message) {
        if (!started) {
            started = true;
            startTime = System.currentTimeMillis();
        }
        if (cancelled) {
            throw new OperationCancelledException();
        }
        if (paused) {
            while (paused) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        }
        if ((progress < 0 || progress > 1) && !Double.isNaN(progress)) {
            if (ProgressMonitorFactory.Config.isStrictComputationMonitor()) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress);
            } else {
                if (progress < 0) {
                    progress = 0;
                } else if (progress > 1) {
                    progress = 1;
                }
            }
        }
        if (ProgressMonitorFactory.Config.isStrictComputationMonitor()) {
            if (!Double.isNaN(progress) && progress < getProgressValue() && getProgressValue() >= 1) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress + "<" + getProgressValue());
            }
        }
        setProgressImpl(progress, message);
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isCanceled() {
        return cancelled;
    }
    public ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) {
        this.incrementor = incrementor;
        return this;
    }

    public ProgressMonitorInc getIncrementor() {
        return this.incrementor;
    }

    protected abstract void setProgressImpl(double progress, ProgressMessage message);
}
