package net.vpc.common.mon;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:36:26
 */
public class DefaultProgressMonitor extends AbstractProgressMonitor {

    public DefaultProgressMonitor() {
        super(nextId());
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

}
