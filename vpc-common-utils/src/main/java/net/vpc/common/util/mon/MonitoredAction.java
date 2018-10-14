package net.vpc.common.util.mon;

/**
 * Created by vpc on 5/14/17.
 */
public interface MonitoredAction<T> {
    T process(ProgressMonitor monitor, String messagePrefix) throws Exception;
}
