package net.thevpc.common.prs.factory;

public interface FactoryVetoListener {
    public void instanceCreated(FactoryEvent event) throws FactoryVetoException;
}
