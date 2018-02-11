package net.vpc.common.prs.factory;

public interface FactoryVetoListener {
    public void instanceCreated(FactoryEvent event) throws FactoryVetoException;
}
