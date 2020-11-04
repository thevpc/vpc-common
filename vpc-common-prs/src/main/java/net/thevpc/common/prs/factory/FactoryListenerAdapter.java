package net.thevpc.common.prs.factory;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)  alias vpc
 * @creationtime 2009/08/16 18:19:03
 */
public abstract class FactoryListenerAdapter implements FactoryListener{
    public void implementationSelectionChanged(FactoryEvent e) {
        //ignore
    }

    public void implementationDefaultChanged(FactoryEvent e) {
        //ignore
    }

    public void implementationAdded(FactoryEvent e) {
        //ignore
    }

    public void alternativeAdded(FactoryEvent e) {
        //ignore
    }

    public void alternativeRemoved(FactoryEvent e) {
        //ignore
    }

    public void instanceCreated(FactoryEvent event) {
        //ignore
    }

    public void extensionFactoryAdded(FactoryEvent e) {
        //ignore
    }

    public void extensionFactoryRemoved(FactoryEvent e) {
        //ignore
    }

    public void implementationRemoved(FactoryEvent e) {
        //ignore
    }
    
    
}
