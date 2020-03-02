package net.vpc.common.mon;


public interface TaskMonitorManager extends ProgressMonitorFactory {
//    void addTask(PlotThread task);

    void addTask(net.vpc.common.mon.ProgressMonitor task, String windowTitle, String descLabel);

    net.vpc.common.mon.ProgressMonitor createMonitor(String name, String description);

    void removeTask(TaskMonitor component);

    void addListener(TaskMonitorManagerListener listener);

    void removeListener(TaskMonitorManagerListener listener);

    TaskMonitorManagerListener[] getListeners();

    TaskMonitor[] getTasks();

    void resetMonitors();

    void terminateAll();

    void unfreeze();

    void ticMonitor();

    int getTasksCount();

    void waitForTask();

    void removeTask(long id);
}
