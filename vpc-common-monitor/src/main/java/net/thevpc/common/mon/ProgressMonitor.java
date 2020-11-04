package net.thevpc.common.mon;

import net.thevpc.common.msg.Message;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:34:27
 */
public interface ProgressMonitor extends TaskMonitor {
    double INDETERMINATE_PROGRESS = Double.NaN;

    double getProgress();


    /**
     * [0..1]
     *
     * @param progress
     * @param message
     */
    void setProgress(double progress, Message message);

    ProgressMonitor[] split(int nbrElements);

    ProgressMonitor[] split(double... weight);

    ProgressMonitor translate(double factor, double start);

    ProgressMonitor translate(int index, int max);

    void setProgress(double i);

    void setProgress(int i, int max);

    void setProgress(int i, int max, String message);

    void setProgress(int i, int max, String message, Object... args);

    void setProgress(int i, int maxi, int j, int maxj, String message);

    ProgressMonitor translate(int i, int imax, int j, int jmax);

    void setProgress(int i, int j, int maxi, int maxj, String message, Object... args);

    void setProgress(double progress, String message);

    void setProgress(double progress, String message, Object... args);

    ProgressMonitor setIndeterminate(String message);

    ProgressMonitor setIndeterminate(String message, Object... args);


    ProgressMonitor incremental(int iterations);

    ProgressMonitor incremental(ProgressMonitor baseMonitor, double delta);

    ProgressMonitor temporize(long freq);

    ProgressMonitorInc getIncrementor();

    ProgressMonitor setIncrementor(ProgressMonitorInc incrementor);

    ProgressMonitor inc();

    ProgressMonitor inc(String message);

    ProgressMonitor inc(String message, Object... args);

    long getEstimatedTotalDuration();

    long getEstimatedRemainingDuration();

    ProgressMonitor terminate(String message);

    ProgressMonitor terminate(String message, Object... args);

    ProgressMonitor start(String message);

    ProgressMonitor start(String message, Object... args);


}
