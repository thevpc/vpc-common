package net.thevpc.common.mon;

import java.util.logging.Level;

import net.thevpc.common.msg.Message;

public class FreqProgressMonitor extends AbstractProgressMonitor {
    private long freq;
    private long lastMessageDate;
    private long lastProgressDate;
    private double progress;
    private Level level = Level.INFO;
    private ProgressMonitor delegate;

    public FreqProgressMonitor(ProgressMonitor delegate, long freq) {
        this.delegate = delegate;
        if (freq < 0) {
            freq = 0;
        }
        this.freq = freq;
    }

    public ProgressMonitor getDelegate() {
        return delegate;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    public void setProgressImpl(double progress) {
        this.progress = progress;
        long newd = System.currentTimeMillis();
        if (newd > lastProgressDate + freq
                || progress == 0
                || progress == 1
                || Double.isNaN(progress)
        ) {
            getDelegate().setProgress(progress);
            lastProgressDate = newd;
        }
    }

    public void setMessageImpl(Message message) {
        long newd = System.currentTimeMillis();
        if (message.getLevel().intValue() >= level.intValue() || newd > lastMessageDate + freq) {
            getDelegate().setMessage(message);
            lastMessageDate = newd;
        }
    }

    @Override
    public Message getMessage() {
        return getDelegate().getMessage();
    }

    @Override
    public String toString() {
        return "Freq(" +
                "value=" + getProgress() +
                ",time=" + freq +
                ", " + getDelegate() +
                ')';
    }
}
