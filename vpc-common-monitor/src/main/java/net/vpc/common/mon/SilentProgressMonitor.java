package net.vpc.common.mon;

import net.vpc.common.msg.Message;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class SilentProgressMonitor extends AbstractProgressMonitor {
    private double progress=Double.NaN;
    private Message message=EMPTY_MESSAGE;
    public SilentProgressMonitor() {
        super();
    }

    @Override
    protected void setProgressImpl(double progress) {
        this.progress=progress;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    protected void setMessageImpl(Message message) {
        this.message=message;
    }

    @Override
    public String toString() {
        return "Silent(" +
                "value=" + getProgress() +
                ')';
    }
}
