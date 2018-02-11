package net.vpc.common.prs.util;


public class PartialProgressMonitor implements ProgressMonitor{
    private final ProgressMonitor monitor;
    private final float start;
    private final float length;
    public PartialProgressMonitor(ProgressMonitor monitor, float start, float length) {
        this.monitor = monitor;
        this.start = start;
        this.length = length;
    }

    @Override
    public void progressStart(ProgressEvent e) {
        monitor.progressStart(new ProgressEvent(e.getSource(),e.isIndeterminate(),convertProgress(0f),e.getMessage(), e.getMessageParameters()));
    }

    @Override
    public void progressUpdate(ProgressEvent e) {
        monitor.progressUpdate(new ProgressEvent(e.getSource(),e.isIndeterminate(),convertProgress(e.getProgress()),e.getMessage(), e.getMessageParameters()));
    }

    @Override
    public void progressEnd(ProgressEvent e) {
        monitor.progressEnd(new ProgressEvent(e.getSource(),e.isIndeterminate(),convertProgress(1f),e.getMessage(), e.getMessageParameters()));
    }

    private float convertProgress(float p){
        return p/length+start;
    }
}
