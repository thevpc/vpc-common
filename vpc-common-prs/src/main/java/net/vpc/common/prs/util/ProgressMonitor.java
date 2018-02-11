package net.vpc.common.prs.util;

public interface ProgressMonitor {
    public static final ProgressMonitor NONE=new ProgressMonitor() {
        public void progressStart(ProgressEvent e) {
            //Nothing
        }

        public void progressUpdate(ProgressEvent e) {
            //Nothing
        }

        public void progressEnd(ProgressEvent e) {
            //Nothing
        }
    };
    public void progressStart(ProgressEvent e);
    public void progressUpdate(ProgressEvent e);
    public void progressEnd(ProgressEvent e);
}
