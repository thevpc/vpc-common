package net.vpc.common.mon;

public class TaskMonitorDelegate implements TaskMonitor {
    private TaskMonitor delegate;

    public TaskMonitorDelegate(TaskMonitor delegate) {
        this.delegate = delegate;
    }

    protected TaskMonitor getDelegate() {
        return delegate;
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void terminate() {
        delegate.terminate();
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }

    @Override
    public void resume() {
        delegate.resume();
    }

    @Override
    public void suspend() {
        delegate.suspend();
    }

    @Override
    public boolean isSuspended() {
        return delegate.isSuspended();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean isBlocked() {
        return delegate.isBlocked();
    }

    @Override
    public void setBlocked(boolean blocked) {
        delegate.setBlocked(blocked);
    }

    @Override
    public boolean isStarted() {
        return delegate.isStarted();
    }

    @Override
    public boolean isCanceled() {
        return delegate.isCanceled();
    }

    @Override
    public void reset() {
        delegate.reset();
    }

    @Override
    public long getId() {
        return delegate.getId();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDesc() {
        return delegate.getDesc();
    }

    @Override
    public void addListener(TaskListener listener) {
        delegate.addListener(listener);
    }

    @Override
    public void removeListener(TaskListener listener) {
        delegate.removeListener(listener);
    }

    @Override
    public TaskListener[] getListeners() {
        return delegate.getListeners();
    }

    @Override
    public long getDuration() {
        return getDelegate().getDuration();
    }

    @Override
    public long getStartTime() {
        return getDelegate().getStartTime();
    }

    @Override
    public void setMessage(TaskMessage message) {
        getDelegate().setMessage(message);
    }

    @Override
    public void setMessage(String message) {
        getDelegate().setMessage(message);
    }

    @Override
    public void setMessage(String message, Object... args) {
        getDelegate().setMessage(message, args);
    }

    @Override
    public TaskMessage getProgressMessage() {
        return getDelegate().getProgressMessage();
    }
}
