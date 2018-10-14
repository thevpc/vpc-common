package net.vpc.common.util.mon;


import net.vpc.common.util.Chronometer;

import java.util.logging.Level;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public abstract class AbstractProgressMonitor implements ProgressMonitor {
    private ProgressMonitorInc incrementor = new DeltaProgressMonitorInc(1E-2);
    private Chronometer chronometer;
    private boolean paused = false;
    private boolean cancelled = false;
    private boolean started = false;

    public AbstractProgressMonitor() {
    }


    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @return ProgressMonitor[] array that contains nulls or  translated baseMonitor
     */









//    @Override
//    public EnhancedProgressMonitor startm(String message) {
//        return start(message + ", starting...");
//    }
//
//    @Override
//    public EnhancedProgressMonitor terminatem(String message, Object... args) {
//        return terminate(message + ", terminated...",args);
//    }
//
//    @Override
//    public EnhancedProgressMonitor terminatem(String message) {
//        return terminate(message + ", terminated...");
//    }


    public ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) {
        this.incrementor = incrementor;
        return this;
    }

    public ProgressMonitorInc getIncrementor() {
        return this.incrementor;
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

    public final void setProgress(double progress, ProgressMessage message) {
       Chronometer c = getChronometer();//
        if (!started) {
            started = true;
            c.start();
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
            if (!Double.isNaN(progress)  && progress < getProgressValue() && getProgressValue() >= 1) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress + "<" + getProgressValue());
            }
        }
        setProgressImpl(progress, message);
    }

    protected abstract void setProgressImpl(double progress, ProgressMessage message);

//    @Override
//    public boolean isTerminated() {
//        return isCanceled() || getProgressValue() >= 1;
//    }

    @Override
    public boolean isCanceled() {
        return cancelled;
    }

    public long getStartTime() {
        return getChronometer().getStartTime();
    }

    @Override
    public Chronometer getChronometer() {
        if (chronometer == null) {
            chronometer = new Chronometer(false);
        }
        return chronometer;
    }



    @Override
    public void stop() {

    }
}
