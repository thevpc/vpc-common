package net.vpc.common.mon;

import static net.vpc.common.mon.AbstractTaskMonitor.EMPTY_MESSAGE;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 19 juil. 2007 00:27:15
 */
public class ProgressMonitorTranslator extends ProgressMonitorDelegate {

    private double start;
    private TaskMessage message;
    private double factor;

    public ProgressMonitorTranslator(ProgressMonitor baseMonitor, double factor, double start) {
        super(baseMonitor);
        if (baseMonitor == null) {
            throw new NullPointerException("baseMonitor could not be null");
        }
        this.factor = factor;
        this.start = start;
    }

    @Override
    public double getProgressValue() {
        double d = (getDelegate().getProgressValue() - start) / factor;
        return d < 0 ? 0 : d > 1 ? 1 : d;
    }

    @Override
    public void setProgress(double progress, TaskMessage message) {
        this.message = message;
        double translatedProgress = Double.isNaN(progress)?progress:(progress * factor + start);
//        double translatedProgress = (progress-start)/factor;
        if (!Double.isNaN(progress) && (translatedProgress < 0 || translatedProgress > 1)) {
            if (translatedProgress > 1 && translatedProgress < 1.1) {
                translatedProgress = 1;
            } else {
                System.err.println("ProgressMonitorTranslator : %= " + translatedProgress + "????????????");
            }
        }
        if (message != null && message instanceof StringPrefixTaskMessage) {
            message = new StringPrefixTaskMessage(
                    ProgressMonitors.PERCENT_FORMAT.format(progress) + " " +
                            ((StringPrefixTaskMessage) message).getPrefix(),
                    ((StringPrefixTaskMessage) message).getMessage()
            );
        } else {
            message = new StringPrefixTaskMessage(
                    ProgressMonitors.PERCENT_FORMAT.format(progress) + " ",
                    message
            );
        }
        getDelegate().setProgress(translatedProgress, message);
//        baseMonitor.setProgress((progress-start)/factor);
    }

    @Override
    public TaskMessage getProgressMessage() {
        return message==null? EMPTY_MESSAGE : message;
    }

    @Override
    public String toString() {
        return "Translator(" +
                "value=" + getProgressValue() +
                ",start=" + start +
                ", factor=" + factor +
                ", " + getDelegate() +
                ')';
    }
}
