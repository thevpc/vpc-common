package net.thevpc.common.mon;

import java.util.logging.Level;

import net.thevpc.common.msg.FormattedMessage;
import net.thevpc.common.msg.Message;
import net.thevpc.common.msg.StringMessage;


public abstract class AbstractProgressMonitor extends AbstractTaskMonitor implements ProgressMonitor {

    private ProgressMonitorInc incrementor = new DeltaProgressMonitorInc(1E-2);
//    private double progress=Double.NaN;

    public AbstractProgressMonitor() {
        this(nextId());
    }

    public AbstractProgressMonitor(long id) {
        super(id);
    }

//    @Override
//    public double getProgress() {
//        return progress;
//    }


    public final void setProgress(double progress, Message message) {
        setProgress(progress);
        setMessage(message);
    }

    public final ProgressMonitor[] split(int nbrElements) {
        return ProgressMonitors.split(this, nbrElements);
    }

    public final ProgressMonitor[] split(double... weight) {
        return ProgressMonitors.split(this, weight);
    }

    public final ProgressMonitor translate(double factor, double start) {
        return ProgressMonitors.translate(this, factor, start);
    }

    public final ProgressMonitor translate(int index, int max) {
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
            if (!Double.isNaN(progress) && progress < getProgress() && getProgress() >= 1) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress + "<" + getProgress());
            }
        }
        if(this.getProgress() != progress){
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

    public final void setProgress(int i, int max) {
        this.setProgress((1.0 * i / max), getMessage());
    }

    public final void setProgress(int i, int max, String message) {
        this.setProgress((1.0 * i / max), message);
    }

    public final void setProgress(int i, int max, String message, Object... args) {
        this.setProgress((1.0 * i / max), message, args);
    }

    public final void setProgress(int i, int maxi, int j, int maxj, String message) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), message);
    }

    @Override
    public final ProgressMonitor translate(int i, int imax, int j, int jmax) {
        return new ProgressMonitorTranslator(this, 1.0 / (imax*jmax), ((1.0 * i * imax) + j) / (imax * jmax));
    }


    public final void setProgress(int i, int j, int maxi, int maxj, String message, Object... args) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), message, args);
    }

    public final void setProgress(double progress, String message) {
        setProgress(progress, new StringMessage(Level.FINE, message));
    }

    public final void setProgress(double progress, String message, Object... args) {
        setProgress(progress, new FormattedMessage(Level.FINE, message, args));
    }

    public final ProgressMonitor setIndeterminate(String message) {
        setProgress(INDETERMINATE_PROGRESS, new StringMessage(Level.FINE, message));
        return this;
    }

    public final ProgressMonitor setIndeterminate(String message, Object... args) {
        setProgress(INDETERMINATE_PROGRESS, new FormattedMessage(Level.FINE, message, args));
        return this;
    }

    public final ProgressMonitor incremental(int iterations) {
        return ProgressMonitors.incremental(this, iterations);
    }

    public final ProgressMonitor incremental(ProgressMonitor baseMonitor, double delta) {
        return ProgressMonitors.incremental(this, delta);
    }

    public final ProgressMonitor temporize(long freq) {
        return ProgressMonitors.temporize(this, freq);
    }

    public final ProgressMonitorInc getIncrementor() {
        return this.incrementor;
    }

    public final ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) {
        this.incrementor = incrementor;
        return this;
    }

    public final ProgressMonitor inc() {
        inc("");
        return this;
    }

    public final ProgressMonitor inc(String message) {
        ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("Missing Incrementor");
        }
        setProgress(incrementor.inc(getProgress()), new StringMessage(Level.FINE, message));
        return this;
    }

    public final ProgressMonitor inc(String message, Object... args) {
        ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("Missing Incrementor");
        }
        setProgress(incrementor.inc(getProgress()), new FormattedMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public final long getEstimatedTotalDuration() {
        double d = getProgress();
        long spent = getDuration();
        if (spent <= 0) {
            return -1;
        }
        return (long) ((spent / d));
    }

    @Override
    public final long getEstimatedRemainingDuration() {
        double d = getProgress();
        long spent = getDuration();
        if (spent <= 0) {
            return -1;
        }
        return (long) ((spent / d) * (1 - d));
    }


    public final ProgressMonitor terminate(String message) {
        if (!isTerminated()) {
            super.terminate();
            setProgress(1, new StringMessage(Level.INFO, message));
        }
        return this;
    }

    public final ProgressMonitor terminate(String message, Object... args) {
        if (!isTerminated()) {
            setProgress(1, new FormattedMessage(Level.INFO, message, args));
        }
        return this;
    }

    public final ProgressMonitor start(String message) {
        if (!isStarted()) {
            setProgress(0, new StringMessage(Level.FINE, message));
        }
        return this;
    }

    public final ProgressMonitor start(String message, Object... args) {
        if (!isStarted()) {
            start();
            setProgress(0, new FormattedMessage(Level.INFO, message, args));
        }
        return this;
    }

    @Override
    protected void startImpl() {
        setProgress(0, new StringMessage(Level.FINE, ""));
    }

    protected void terminateImpl() {
        setProgress(1, new StringMessage(Level.FINE, ""));
    }

    protected abstract void setProgressImpl(double progress);

}

