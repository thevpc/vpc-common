package net.vpc.common.mon;

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

    boolean isStarted();

    boolean isCanceled();

    void stop();

//    void setProgress(double progress, String message);


    ProgressMonitor[] split(int nbrElements);

    ProgressMonitor[] split(double[] weight);


    ProgressMonitor[] split(double[] weight, boolean[] enabledElements);

    ProgressMonitor translate(double factor, double start);

    ProgressMonitor translate(int index, int max);

    void setProgress(double i);

    void setProgress(int i, int max);

    void setProgress(int i, int max, String message);

    void setProgress(int i, int max, String message, Object... args);

    void setProgress(int i, int j, int maxi, int maxj, String message);

    void setProgress(int i, int j, int maxi, int maxj, String message, Object... args);

    void setProgress(double progress, String message);

    void setProgress(double progress, String message, Object... args);

    ProgressMonitor setMessage(ProgressMessage message);

    ProgressMonitor setIndeterminate(String message);

    ProgressMonitor setMessage(String message, Object... args);

    ProgressMonitor setIndeterminate(String message, Object... args);


    ProgressMonitor setMessage(String message);

    ProgressMonitor start(String message);

    ProgressMonitor start(String message, Object... args);

    ProgressMonitor terminate(String message);


    ProgressMonitor terminate(String message, Object... args);

    ProgressMonitor createIncrementalMonitor(int iterations);

    ProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, double delta);

    ProgressMonitor temporize(long freq);

    ProgressMonitor[] split(boolean... enabledElements);

    ProgressMonitorInc getIncrementor();

    ProgressMonitor setIncrementor(ProgressMonitorInc incrementor);

    ProgressMonitor inc();

    ProgressMonitor inc(String message);

    ProgressMonitor inc(String message, Object... args);

    ProgressMonitor cancel();

    ProgressMonitor resume();

    ProgressMonitor suspend();

    long getDuration();

    long getStartTime();

    boolean isTerminated();

}
