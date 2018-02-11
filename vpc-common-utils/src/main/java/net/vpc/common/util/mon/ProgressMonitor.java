package net.vpc.common.util.mon;

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


}
