package net.vpc.common.app.swing.core.swing;

import net.vpc.common.app.Application;

public class DefaultNodeSupplierContext implements NodeSupplierContext {
    private Object parentGuiComponent;
    private Application applicatino;
    private BindingNodeFactory factory;

    public DefaultNodeSupplierContext(Object parentGuiComponent, Application applicatino, BindingNodeFactory factory) {
        this.parentGuiComponent = parentGuiComponent;
        this.applicatino = applicatino;
        this.factory = factory;
    }

    public <T> T getParentGuiComponent() {
        return (T) parentGuiComponent;
    }

    @Override
    public Application getApplication() {
        return applicatino;
    }

    @Override
    public BindingNodeFactory getFactory() {
        return factory;
    }
}
