package net.vpc.common.mon;

import net.vpc.common.msg.Message;

import java.awt.*;
import java.util.logging.Level;

public class DialogProgressMonitor extends AbstractProgressMonitor {
    private javax.swing.ProgressMonitor monitor;
    private Message message=EMPTY_MESSAGE;
    private double progress=Double.NaN;
    public DialogProgressMonitor(Component parentComponent, Object message) {
        super(nextId());
        monitor = new javax.swing.ProgressMonitor(parentComponent, message, null, 0, 100);
        //monitor.pane
     }


    @Override
    public void setProgressImpl(double progress) {
        this.progress=progress;
        monitor.setProgress((int) (100 * progress));
    }

    @Override
    protected void setMessageImpl(Message message) {
        this.message=message;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public boolean isCanceled() {
        return monitor.isCanceled();
    }

    @Override
    public void terminate() {
        super.terminate();
        monitor.close();
    }
}
