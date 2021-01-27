package net.thevpc.common.prs.util;

public class StepProgressMonitor implements ProgressMonitor{
    private ProgressMonitor monitor;
    private int count;
    private float step;
    private float current=0;

    public StepProgressMonitor(ProgressMonitor monitor, int count) {
        this.monitor = monitor;
        this.count = count;
        step=1f/count;
    }

    public void progressStart(Object source,String message,Object... params) {
        monitor.progressStart(new ProgressEvent(source,current=0f,message,params));
    }

    public void progressEnd(Object source,String message,Object... params) {
        monitor.progressStart(new ProgressEvent(source,current=1f,message,params));
    }

    public void progressStep(Object source,String message,Object... params) {
        monitor.progressStart(new ProgressEvent(source,current+=step,message,params));
    }

    public void progressStart(ProgressEvent e) {
        monitor.progressStart(e);
    }

    public void progressUpdate(ProgressEvent e) {
        monitor.progressUpdate(e);
    }

    public void progressEnd(ProgressEvent e) {
        monitor.progressEnd(e);
    }
}
