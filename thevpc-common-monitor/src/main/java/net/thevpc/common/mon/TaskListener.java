package net.thevpc.common.mon;

public interface TaskListener {

    void taskStarted(TaskMonitor task);

    void taskResumed(TaskMonitor task);

    void taskSuspended(TaskMonitor task);

    void taskCanceled(TaskMonitor task);

    void taskTerminated(TaskMonitor task);

    void taskReset(TaskMonitor task);

    void taskBlocked(TaskMonitor monitor);

    void taskUnblocked(TaskMonitor monitor);
}
