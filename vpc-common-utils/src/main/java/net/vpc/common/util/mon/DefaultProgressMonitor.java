package net.vpc.common.util.mon;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:36:26
 */
public class DefaultProgressMonitor extends AbstractProgressMonitor {
    protected double progress;
    ProgressMessage progressMessage;

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        if(progress<0 || progress>1){
            System.err.println("%= "+progress+"????????????");
        }
        this.progress=progress;
        this.progressMessage=message;
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return progressMessage;
    }

    @Override
    public String toString() {
        return "Default(" +
                "value=" + getProgressValue() +
                ')';
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void stop() {

    }
}
