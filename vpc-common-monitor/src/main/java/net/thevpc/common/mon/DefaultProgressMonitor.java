package net.thevpc.common.mon;

import net.thevpc.common.msg.Message;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:36:26
 */
public class DefaultProgressMonitor extends AbstractProgressMonitor {
    private double progress;
    private Message message=EMPTY_MESSAGE;
    public DefaultProgressMonitor() {
        super(nextId());
    }


    @Override
    public String toString() {
        return "Default(" +
                "value=" + getProgress() +
                ')';
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    protected void setProgressImpl(double progress) {
        this.progress=progress;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    protected void setMessageImpl(Message message) {
        this.message=message;
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
