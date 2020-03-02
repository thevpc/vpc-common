package net.vpc.common.mon;

public interface TaskMonitor {
    //    ProgressMonitor getProgressMonitor();
    void start();

    void terminate();

    void cancel();

    void resume();

    void suspend();

    boolean isSuspended();

    boolean isTerminated();

    boolean isBlocked();

    void setBlocked(boolean blocked);

    boolean isStarted();

    boolean isCanceled();

    void reset();

    long getId();

    String getName();

    String getDesc();

    void addListener(TaskListener listener);

    void removeListener(TaskListener listener);

    TaskListener[] getListeners();

    long getDuration();

    long getStartTime();

    void setMessage(TaskMessage message);

    void setMessage(String message);

    void setMessage(String message, Object... args);

    TaskMessage getProgressMessage();

}
