package net.vpc.common.mon;

import java.awt.*;
import java.util.logging.Level;

public class DialogProgressMonitor extends AbstractProgressMonitor {
    private javax.swing.ProgressMonitor monitor;

    public DialogProgressMonitor(Component parentComponent, Object message) {
        super(nextId());
        monitor = new javax.swing.ProgressMonitor(parentComponent, message, null, 0, 100);
        //monitor.pane
     }


    @Override
    public void setProgressImpl(double progress) {
        monitor.setProgress((int) (100 * progress));
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
