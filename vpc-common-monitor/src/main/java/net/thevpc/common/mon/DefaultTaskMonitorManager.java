package net.thevpc.common.mon;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DefaultTaskMonitorManager implements TaskMonitorManager {
    private List<TaskMonitorManagerListener> listeners = new ArrayList<>();
    private TreeMap<Long, TaskMonitor> tasks = new TreeMap<>();
    private SerializerThread serializerThread = null;
    private int parallelTasksCount = 1;
    private MyTaskListener listener = new MyTaskListener();

    @Override
    public void addTask(ProgressMonitor task, String name, String desc) {
        TaskMonitor oldTask = tasks.remove(task.getId());
        if (oldTask != null) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskRemoved(this, oldTask);
            }
        }
        tasks.put(task.getId(), task);
        task.addListener(listener);
        for (TaskMonitorManagerListener listener : listeners) {
            listener.taskAdded(DefaultTaskMonitorManager.this, task);
        }
    }

    @Override
    public ProgressMonitor createMonitor(String name, String description) {
        ProgressMonitor m = ProgressMonitors.logger(5000);
//                new PlotConsoleLogProgressMonitor(null, plotConsole.getLog()).temporize(5000);
        addTask(m, name, description);
        return m;
    }

    @Override
    public void removeTask(TaskMonitor task) {
        removeTask(task.getId());
    }

    @Override
    public void addListener(TaskMonitorManagerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(TaskMonitorManagerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public TaskMonitorManagerListener[] getListeners() {
        return listeners.toArray(new TaskMonitorManagerListener[0]);
    }

    @Override
    public TaskMonitor[] getTasks() {
        return tasks.values().toArray(new TaskMonitor[0]);
    }

    @Override
    public void resetMonitors() {
        for (TaskMonitor task : tasks.values()) {
            task.reset();
        }
    }

    @Override
    public void terminateAll() {
        for (TaskMonitor t : getTasks()) {
            t.terminate();
        }
        try {
            if (serializerThread != null) {
                serializerThread.softStop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unfreeze() {
        if (serializerThread != null) {
            serializerThread.softStop();
            serializerThread = null;
        }
    }

    @Override
    public void ticMonitor() {
        final TaskMonitor[] tasks = getTasks();
        for (int i = 0; i < tasks.length; i++) {
            final TaskMonitor t = tasks[i];
            if (t.isTerminated()) {
                removeTask(t);
            }
        }
    }

    @Override
    public int getTasksCount() {
        return tasks.size();
    }

    @Override
    public void waitForTask() {
        if (getParallelTasksCount() <= getTasksCount()) {
//                                    System.out.println("starting synchronous "+windowTitle+"/" +yParam);
            if (serializerThread != null) {
                System.out.println(">>>>>>>>>>serializerThread!=null");
            }
            serializerThread = new SerializerThread();
            serializerThread.serialize();
            serializerThread = null;
        } else {
//                                    System.out.println("starting asynchronous "+windowTitle+"/" +yParam);
        }
    }

    @Override
    public void removeTask(long id) {
        TaskMonitor removed = tasks.remove(id);
        if (removed != null) {
            if (serializerThread != null) {
                if (parallelTasksCount > getTasksCount()) {
                    serializerThread.softStop();
                    serializerThread = null;
                }
            }
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskRemoved(this, removed);
            }
        }
    }

    public int getParallelTasksCount() {
        return parallelTasksCount;
    }

    abstract class UserStopThread extends Thread {
        boolean stopped;
        long sleep;

        protected UserStopThread(long sleep) {
            super("UserStopThread");
            setDaemon(true);
            this.sleep = sleep;
        }

        public boolean isStopped() {
            return stopped;
        }

        public void softStop() {
            this.stopped = true;
        }

        public void run() {
            while (!stopped) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                execute();
            }
        }

        public abstract void execute();
    }

    public class SerializerThread extends UserStopThread {
        public SerializerThread() {
            super(1000);
            setName("SerializerThread");
        }

        public void serialize() {
            start();
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() {
        }
    }

    private class MyTaskListener implements TaskListener {
        @Override
        public void taskStarted(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskStarted(DefaultTaskMonitorManager.this, task);
            }
        }

        @Override
        public void taskResumed(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskResumed(DefaultTaskMonitorManager.this, task);
            }
        }

        @Override
        public void taskSuspended(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskSuspended(DefaultTaskMonitorManager.this, task);
            }
        }

        @Override
        public void taskCanceled(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskCanceled(DefaultTaskMonitorManager.this, task);
            }
            removeTask(task);
        }

        @Override
        public void taskTerminated(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskTerminated(DefaultTaskMonitorManager.this, task);
            }
            removeTask(task);
        }

        @Override
        public void taskReset(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskReset(DefaultTaskMonitorManager.this, task);
            }

        }

        @Override
        public void taskBlocked(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskBlocked(DefaultTaskMonitorManager.this, task);
            }

        }

        @Override
        public void taskUnblocked(TaskMonitor task) {
            for (TaskMonitorManagerListener listener : listeners) {
                listener.taskUnblocked(DefaultTaskMonitorManager.this, task);
            }
        }
    }
}
