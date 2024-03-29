package net.thevpc.common.mon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import net.thevpc.common.msg.JFormattedMessage;
import net.thevpc.common.msg.Message;
import net.thevpc.common.msg.StringMessage;

public abstract class AbstractTaskMonitor implements TaskMonitor {
    public static final StringMessage EMPTY_MESSAGE = new StringMessage(Level.INFO, "");
    private static long _NEXT_ID = 0;
    protected MonChronometer chronometer = new MonChronometer();
    private boolean suspended = false;
    private boolean cancelled = false;
    private boolean started = false;
    private boolean terminated = false;
    private boolean blocked = false;
    private List<TaskListener> listeners = new ArrayList<>();
    private long id;
    private String name;
    private String desc;

    public AbstractTaskMonitor(long id) {
        this.id = id;
    }

    public static long nextId() {
        return ++_NEXT_ID;
    }

    protected void setStartedImpl() {

    }

    @Override
    public void start() {
        if (!isStarted()) {
            started = true;
            chronometer.start();
            startImpl();
            for (TaskListener listener : getListeners()) {
                listener.taskStarted(this);
            }
        }
    }

    @Override
    public void terminate() {
        terminate(true);
        if (!isTerminated()) {
            if (!isStarted()) {
                start();
            }
            terminated = true;
            terminateImpl(terminated);
            for (TaskListener listener : getListeners()) {
                listener.taskTerminated(this);
            }
        }
    }

    protected void terminate(boolean terminated) {
        if(terminated) {
            if (!isTerminated()) {
                if (!isStarted()) {
                    start();
                }
                this.terminated = true;
                terminateImpl(terminated);
                for (TaskListener listener : getListeners()) {
                    listener.taskTerminated(this);
                }
            }
        }else{
            if (isTerminated()) {
                this.terminated = false;
                terminateImpl(false);
                for (TaskListener listener : getListeners()) {
                    listener.taskStarted(this);
                }
            }
        }
    }

    @Override
    public void cancel() {
        if (!isCanceled()) {
            cancelled = true;
            cancelImpl();
            for (TaskListener listener : getListeners()) {
                listener.taskCanceled(this);
            }
        }
    }

    @Override
    public void resume() {
        if (suspended) {
            suspended = false;
            resumeImpl();
            for (TaskListener listener : getListeners()) {
                listener.taskResumed(this);
            }
        }
    }

    @Override
    public void suspend() {
        if (!suspended) {
            suspended = true;
            suspendImpl();
            for (TaskListener listener : getListeners()) {
                listener.taskSuspended(this);
            }
        }
    }

    public boolean isSuspended() {
        return suspended;
    }

    public boolean isTerminated() {
        return terminated;
    }

    @Override
    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public void setBlocked(boolean blocked) {
        if (blocked != this.blocked) {
            this.blocked = blocked;
            setBlockedImpl(blocked);
            if (blocked) {
                for (TaskListener listener : getListeners()) {
                    listener.taskBlocked(this);
                }
            } else {
                for (TaskListener listener : getListeners()) {
                    listener.taskUnblocked(this);
                }
            }
        }
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isCanceled() {
        return cancelled;
    }

    public void reset() {
        if (isStarted() || isCanceled() || isTerminated() || isSuspended() || isBlocked()) {
            suspended = false;
            cancelled = false;
            started = false;
            terminated = false;
            blocked = false;
            chronometer.reset();
            resetImpl();
            for (TaskListener listener : listeners) {
                listener.taskReset(this);
            }
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    protected void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public void addListener(TaskListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(TaskListener listener) {
        listeners.remove(listener);
    }

    @Override
    public TaskListener[] getListeners() {
        return listeners.toArray(new TaskListener[0]);
    }

    public long getDuration() {
        return chronometer.getDuration();
    }

    public long getStartTime() {
        return chronometer.getStartTime();
    }

    protected void setBlockedImpl(boolean blocked) {

    }

//    @Override
//    public void stop() {
//        if (!stopped) {
//            stopped = true;
//            for (TaskListener listener : listeners) {
//                listener.progressStopped(this);
//            }
//        }
//    }

    protected void startImpl() {

    }

    protected void terminateImpl(boolean terminated) {

    }

    protected void cancelImpl() {

    }

    protected void resumeImpl() {

    }

    protected void suspendImpl() {

    }

    protected void resetImpl() {

    }

    @Override
    public final void setMessage(Message message) {
        Message newMessage = message == null ? new StringMessage(Level.FINE, null) : message;
        if (!Objects.equals(getMessage(), newMessage)) {
            setMessageImpl(newMessage);
        }
    }

    @Override
    public final void setMessage(String message) {
        setMessage(new StringMessage(Level.FINE, message));
    }

    @Override
    public void setMessage(String message, Object... args) {
        setMessage(message == null ? EMPTY_MESSAGE :
                new JFormattedMessage(Level.FINE, message, args));
    }

    protected abstract void setMessageImpl(Message message);

    protected static class MonChronometer implements Serializable {

        private final static long serialVersionUID = 1L;
        private long accumulated;
        private long startDate;
        private long endDate;
        private String name;
        private long lastTime;
        private boolean running;

        public MonChronometer() {
            start();
        }

        public MonChronometer(String name) {
            this.name = name;
            start();
        }

        public MonChronometer reset() {
            this.accumulated = 0;
            this.startDate = 0;
            this.endDate = 0;
            this.lastTime = 0;
            this.running = false;
            return this;
        }

        public MonChronometer copy() {
            MonChronometer c = new MonChronometer();
            c.name = name;
            c.endDate = endDate;
            c.startDate = startDate;
            c.accumulated = accumulated;
            c.lastTime = lastTime;
            c.running = running;
            return c;
        }

        /**
         * restart chronometer and returns a stopped snapshot/copy of the current
         *
         * @return
         */
        public MonChronometer restart() {
            stop();
            MonChronometer c = copy();
            start();
            return c;
        }

        /**
         * restart chronometer with new name and returns a stopped snapshot/copy of
         * the current (with old name)
         *
         * @param newName
         * @return
         */
        public MonChronometer restart(String newName) {
            stop();
            MonChronometer c = copy();
            setName(newName);
            start();
            return c;
        }

        public MonChronometer updateDescription(String desc) {
            setName(desc);
            return this;
        }

        public String getName() {
            return name;
        }

        public MonChronometer setName(String desc) {
            this.name = desc;
            return this;
        }

        public boolean isStarted() {
            return startDate != 0;
        }

        public boolean isStopped() {
            return endDate == 0;
        }

        public MonChronometer start() {
            endDate = 0;
            startDate = System.nanoTime();
            lastTime = startDate;
            accumulated = 0;
            running = true;
            return this;
        }

        public MonChronometer accumulate() {
            if (running) {
                long n = System.nanoTime();
                accumulated += n - lastTime;
                lastTime = n;
            }
            return this;
        }

        public long lap() {
            if (running) {
                long n = System.nanoTime();
                long lapValue = n - lastTime;
                this.accumulated += lapValue;
                lastTime = n;
                return lapValue;
            }
            return 0;
        }

        public boolean isSuspended() {
            return !running;
        }

        public MonChronometer suspend() {
            if (running) {
                long n = System.nanoTime();
                accumulated += n - lastTime;
                lastTime = -1;
                running = false;
            }
            return this;
        }

        public MonChronometer resume() {
            if (!running) {
                lastTime = System.nanoTime();
                running = true;
            }
            return this;
        }

        public MonChronometer stop() {
            if (running) {
                endDate = System.nanoTime();
                accumulated += endDate - lastTime;
                lastTime = -1;
                running = false;
            }
            return this;
        }

        public long getStartTime() {
            return startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public long getDuration() {
            if (startDate == 0) {
                return 0;
            }
            long curr = ((endDate <= 0) ? System.nanoTime() : endDate) - lastTime;
            return (curr + accumulated);

        }
    }


}
