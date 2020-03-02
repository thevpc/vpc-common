package net.vpc.common.mon;

import java.util.logging.Level;

import static net.vpc.common.mon.AbstractTaskMonitor.EMPTY_MESSAGE;

public class FreqProgressMonitor extends ProgressMonitorDelegate {
    private long freq;
    private long lastMessageDate;
    private long lastProgressDate;
    private double progress;
    private TaskMessage message;
    private Level level = Level.INFO;

    public FreqProgressMonitor(ProgressMonitor base, long freq) {
        super(base);
        if (freq < 0) {
            freq = 0;
        }
        this.freq = freq;
    }

    @Override
    public double getProgressValue() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        long newd = System.currentTimeMillis();
        if (newd > lastProgressDate + freq
                || progress == 0
                || progress == 1
                || Double.isNaN(progress)
        ) {
            getDelegate().setProgress(progress, message);
            lastProgressDate = newd;
        }
    }

    public void setMessage(TaskMessage message) {
        this.message = message;
        long newd = System.currentTimeMillis();
        if (message.getLevel().intValue() >= level.intValue() || newd > lastMessageDate + freq) {
            getDelegate().setMessage(message);
            lastMessageDate = newd;
        }
    }

    @Override
    public TaskMessage getProgressMessage() {
        return message==null? AbstractTaskMonitor.EMPTY_MESSAGE : message;
    }

    @Override
    public String toString() {
        return "Freq(" +
                "value=" + getProgressValue() +
                ",time=" + freq +
                ", " + getDelegate() +
                ')';
    }
}
