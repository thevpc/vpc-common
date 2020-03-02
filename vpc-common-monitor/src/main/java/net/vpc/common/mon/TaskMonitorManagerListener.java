package net.vpc.common.mon;

public interface TaskMonitorManagerListener {
    void taskAdded(TaskMonitorManager manager, TaskMonitor task);

    void taskRemoved(TaskMonitorManager manager, TaskMonitor task);

    void taskStarted(TaskMonitorManager manager, TaskMonitor task);

    void taskResumed(TaskMonitorManager manager, TaskMonitor task);

    void taskSuspended(TaskMonitorManager manager, TaskMonitor task);

    void taskCanceled(TaskMonitorManager manager, TaskMonitor task);

    void taskTerminated(TaskMonitorManager manager, TaskMonitor task);

    void taskReset(TaskMonitorManager manager, TaskMonitor task);

    void taskBlocked(TaskMonitorManager manager, TaskMonitor monitor);

    void taskUnblocked(TaskMonitorManager manager, TaskMonitor monitor);
}
