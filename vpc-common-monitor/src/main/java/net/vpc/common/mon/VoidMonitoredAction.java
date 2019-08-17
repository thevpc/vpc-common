package net.vpc.common.mon;

/**
 * Created by vpc on 5/14/17.
 */
public abstract class VoidMonitoredAction implements MonitoredAction<Boolean> {
    @Override
    public final Boolean process(ProgressMonitor monitor, String messagePrefix) throws Exception {
        this.invoke(monitor, messagePrefix);
        return true;
    }

    public abstract void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception;
}
