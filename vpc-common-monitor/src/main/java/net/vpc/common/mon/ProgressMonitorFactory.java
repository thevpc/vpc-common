package net.vpc.common.mon;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:34:27
 */
public interface ProgressMonitorFactory {

    ProgressMonitor createMonitor(String name,String description);

}
