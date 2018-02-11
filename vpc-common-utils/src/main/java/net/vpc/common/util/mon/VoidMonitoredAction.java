package net.vpc.common.util.mon;

/**
 * Created by vpc on 5/14/17.
 */
public abstract class VoidMonitoredAction implements MonitoredAction<Boolean> {
    @Override
    public final Boolean process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
        this.invoke(monitor, messagePrefix);
        return true;
    }

    public abstract void invoke(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception;
}
