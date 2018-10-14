package net.vpc.common.util.mon;

import net.vpc.common.util.Chronometer;

import java.util.logging.Level;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:34:27
 */
public interface ProgressMonitor {
    double INDETERMINATE_PROGRESS = Double.NaN;

    double getProgressValue();

    ProgressMessage getProgressMessage();

    /**
     * [0..1]
     *
     * @param progress
     * @param message
     */
    void setProgress(double progress, ProgressMessage message);

    boolean isCanceled();

    void stop();

//    void setProgress(double progress, String message);


    default ProgressMonitor[] split(int nbrElements) {
        return ProgressMonitorFactory.split(this, nbrElements);
    }

    default ProgressMonitor[] split(double[] weight) {
        return ProgressMonitorFactory.split(this, weight);
    }


    default ProgressMonitor[] split(double[] weight, boolean[] enabledElements) {
        return ProgressMonitorFactory.split(this, weight, enabledElements);
    }

    default ProgressMonitor translate(double factor, double start) {
        return ProgressMonitorFactory.translate(this, factor, start);
    }

    default ProgressMonitor translate(int index, int max) {
        return new ProgressMonitorTranslator(this, 1.0 / max, index * (1.0 / max));
    }

    default void setProgress(double i) {
        setProgress(i, getProgressMessage());
    }

    default void setProgress(int i, int max) {
        this.setProgress((1.0 * i / max), getProgressMessage());
    }

    default void setProgress(int i, int max, String message) {
        this.setProgress((1.0 * i / max), new StringProgressMessage(Level.FINE, message));
    }

    default void setProgress(int i, int max, String message, Object... args) {
        this.setProgress((1.0 * i / max), new FormattedProgressMessage(Level.FINE, message, args));
    }

    default void setProgress(int i, int j, int maxi, int maxj, String message) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new StringProgressMessage(Level.FINE, message));
    }

    default void setProgress(int i, int j, int maxi, int maxj, String message, Object... args) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new FormattedProgressMessage(Level.FINE, message, args));
    }

    default void setProgress(double progress, String message) {
        setProgress(progress, new StringProgressMessage(Level.FINE, message));
    }

    default void setProgress(double progress, String message, Object... args) {
        setProgress(progress, new FormattedProgressMessage(Level.FINE, message, args));
    }

    default ProgressMonitor setMessage(ProgressMessage message) {
        setProgress(getProgressValue(), message);
        return this;
    }

    default ProgressMonitor setIndeterminate(String message) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new StringProgressMessage(Level.FINE, message));
        return this;
    }

    default ProgressMonitor setMessage(String message, Object... args) {
        setProgress(getProgressValue(), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    default ProgressMonitor setIndeterminate(String message, Object... args) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }


    default ProgressMonitor setMessage(String message) {
        setProgress(getProgressValue(), new StringProgressMessage(Level.FINE, message));
        return this;
    }

    default ProgressMonitor start(String message) {
        setProgress(0, new StringProgressMessage(Level.FINE, message));
        return this;
    }

    default ProgressMonitor start(String message, Object... args) {
        setProgress(0, new FormattedProgressMessage(Level.INFO, message, args));
        return this;
    }

    default ProgressMonitor terminate(String message) {
        if (!isCanceled()) {
            setProgress(1, new StringProgressMessage(Level.INFO, message));
        }
        return this;
    }


    default ProgressMonitor terminate(String message, Object... args) {
        if (!isCanceled()) {
            setProgress(1, new FormattedProgressMessage(Level.INFO, message, args));
        }
        return this;
    }

    default ProgressMonitor createIncrementalMonitor(int iterations) {
        return ProgressMonitorFactory.createIncrementalMonitor(this, iterations);
    }

    default ProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, double delta) {
        return ProgressMonitorFactory.createIncrementalMonitor(this, delta);
    }

    default ProgressMonitor temporize(long freq) {
        return ProgressMonitorFactory.temporize(this, freq);
    }

    default ProgressMonitor[] split(boolean... enabledElements) {
        return ProgressMonitorFactory.split(this, enabledElements);
    }

    ProgressMonitorInc getIncrementor() ;

    ProgressMonitor setIncrementor(ProgressMonitorInc incrementor) ;

    default ProgressMonitor inc() {
        inc("");
        return this;
    }

    default ProgressMonitor inc(String message) {
        ProgressMonitorInc incrementor = getIncrementor();
        if(incrementor==null){
            throw new IllegalArgumentException("Missing Incrementor");
        }
        setProgress(incrementor.inc(getProgressValue()), new StringProgressMessage(Level.FINE, message));
        return this;
    }




    default ProgressMonitor inc(String message, Object... args) {
        ProgressMonitorInc incrementor = getIncrementor();
        if(incrementor==null){
            throw new IllegalArgumentException("Missing Incrementor");
        }
        setProgress(incrementor.inc(getProgressValue()), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }


    ProgressMonitor cancel();

    ProgressMonitor resume();

    ProgressMonitor suspend();

    default long getStartTime(){
        return getChronometer().getStartTime();
    }

    Chronometer getChronometer();

    default boolean isTerminated() {
        return getProgressValue() >= 1;
    }

}
