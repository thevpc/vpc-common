package net.thevpc.common.mon;

import net.thevpc.common.msg.Message;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 19 juil. 2007 00:27:15
 */
public class ProgressMonitorTranslator extends AbstractProgressMonitor {

    private double start;
    private double factor;
    private ProgressMonitor delegate;

    public ProgressMonitorTranslator(ProgressMonitor baseMonitor, double factor, double start) {
        this.delegate=baseMonitor;
        if (baseMonitor == null) {
            throw new NullPointerException("baseMonitor could not be null");
        }
        this.factor = factor;
        this.start = start;
    }

    public ProgressMonitor getDelegate() {
        return delegate;
    }

    @Override
    public double getProgress() {
        double d = (getDelegate().getProgress() - start) / factor;
        return d < 0 ? 0 : d > 1 ? 1 : d;
    }

    @Override
    public void setMessageImpl(Message message) {
        getDelegate().setMessage(message);
    }

    @Override
    public Message getMessage() {
        return getDelegate().getMessage();
    }

    @Override
    public void setProgressImpl(double progress) {
        double translatedProgress = Double.isNaN(progress)?progress:(progress * factor + start);
//        double translatedProgress = (progress-start)/factor;
        if (!Double.isNaN(progress) && (translatedProgress < 0 || translatedProgress > 1)) {
            if (translatedProgress > 1 && translatedProgress < 1.1) {
                translatedProgress = 1;
            } else {
                System.err.println("ProgressMonitorTranslator : %= " + translatedProgress + "????????????");
            }
        }
        getDelegate().setProgress(translatedProgress);
//        baseMonitor.setProgress((progress-start)/factor);
    }

    @Override
    public String toString() {
        return "Translator(" +
                "value=" + getProgress() +
                ",start=" + start +
                ", factor=" + factor +
                ", " + getDelegate() +
                ')';
    }
}
