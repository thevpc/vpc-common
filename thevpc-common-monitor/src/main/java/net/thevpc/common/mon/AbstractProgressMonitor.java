package net.thevpc.common.mon;

import java.util.logging.Level;

import net.thevpc.common.msg.JFormattedMessage;
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
    @Override
    public final void setProgress(double progress, Message message) {
        setProgress(progress);
        setMessage(message);
    }

    @Override
    public final ProgressMonitor[] split(int nbrElements) {
        return ProgressMonitors.split(this, nbrElements);
    }

    @Override
    public final ProgressMonitor[] split(double... weight) {
        return ProgressMonitors.split(this, weight);
    }

    @Override
    public final ProgressMonitor translate(double factor, double start) {
        return ProgressMonitors.translate(this, factor, start);
    }

    @Override
    public final ProgressMonitor translate(int index, int max) {
        return new ProgressMonitorTranslator(this, 1.0 / max, index * (1.0 / max));
    }

    @Override
    public final ProgressMonitor stepInto(int index, int max) {
        return new ProgressMonitorTranslator(this, 1.0 / max, index * (1.0 / max));
    }

    @Override
    public final void setProgress(double progress) {
        if (!isStarted()) {
            start();
        }
        if (isCanceled()) {
            return;
        }
        if (isTerminated()) {
            if(progress<1){
                terminate(false);
            }else {
                return;
            }
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
        if (this.getProgress() != progress) {
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
    @Override
    public final void setProgress(int i, int max) {
        this.setProgress((1.0 * i / max), getMessage());
    }

    @Override
    public final void setProgress(int i, int max, String message) {
        this.setProgress((1.0 * i / max), message);
    }

    @Override
    public final void setProgress(int i, int max, String message, Object... args) {
        this.setProgress((1.0 * i / max), message, args);
    }

    @Override
    public final void setProgress(int i, int maxi, int j, int maxj, String message) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), message);
    }

    @Override
    public final ProgressMonitor translate(int i, int imax, int j, int jmax) {
        return new ProgressMonitorTranslator(this, 1.0 / (imax * jmax), ((1.0 * i * imax) + j) / (imax * jmax));
    }

    @Override
    public final void setProgress(int i, int j, int maxi, int maxj, String message, Object... args) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), message, args);
    }

    @Override
    public final void setProgress(double progress, String message) {
        setProgress(progress, new StringMessage(Level.FINE, message));
    }

    @Override
    public final void setProgress(double progress, String message, Object... args) {
        setProgress(progress, new JFormattedMessage(Level.FINE, message, args));
    }

    @Override
    public final ProgressMonitor setIndeterminate(String message) {
        setProgress(INDETERMINATE_PROGRESS, new StringMessage(Level.FINE, message));
        return this;
    }

    @Override
    public final ProgressMonitor setIndeterminate(String message, Object... args) {
        setProgress(INDETERMINATE_PROGRESS, new JFormattedMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public final ProgressMonitor incremental(int iterations) {
        return ProgressMonitors.incremental(this, iterations);
    }

    @Override
    public final ProgressMonitor incremental(ProgressMonitor baseMonitor, double delta) {
        return ProgressMonitors.incremental(this, delta);
    }

    @Override
    public final ProgressMonitor temporize(long freq) {
        return ProgressMonitors.temporize(this, freq);
    }

    @Override
    public final ProgressMonitorInc getIncrementor() {
        return this.incrementor;
    }

    @Override
    public final ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) {
        this.incrementor = incrementor;
        return this;
    }

    @Override
    public final ProgressMonitor inc() {
        inc("");
        return this;
    }

    @Override
    public final ProgressMonitor stepInto(String message) {
        return stepInto(new StringMessage(Level.FINE, message));
    }

    @Override
    public final ProgressMonitor stepInto(String message, Object... msgParams) {
        return stepInto(new JFormattedMessage(Level.FINE, message, msgParams));
    }

    @Override
    public final ProgressMonitor stepInto(Message message) {
        final ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("missing incrementor");
        }
        double a = getProgress();
        double b = incrementor.inc(a);
        setProgress(a, message);
        return new ProgressMonitorTranslator(this, b - a, a);
    }

    @Override
    public final ProgressMonitor inc(String message) {
        ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("missing incrementor");
        }
        double oldProgress=getProgress();
        double newProgress = incrementor.inc(oldProgress);
        setProgress(newProgress, new StringMessage(Level.FINE, message));
        return this;
    }

    @Override
    public final ProgressMonitor inc(String message, Object... args) {
        ProgressMonitorInc incrementor = getIncrementor();
        if (incrementor == null) {
            throw new IllegalArgumentException("missing incrementor");
        }
        double oldProgress = getProgress();
        double newProgress = incrementor.inc(oldProgress);
        setProgress(newProgress, new JFormattedMessage(Level.FINE, message, args));
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
            setProgress(1, new JFormattedMessage(Level.INFO, message, args));
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
            setProgress(0, new JFormattedMessage(Level.INFO, message, args));
        }
        return this;
    }

    @Override
    protected void startImpl() {
        setProgress(0, new StringMessage(Level.FINE, ""));
    }

    protected void terminateImpl(boolean terminated) {
        if(terminated) {
            setProgress(1, new StringMessage(Level.FINE, ""));
        }
    }

    protected abstract void setProgressImpl(double progress);

}
