package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.AppComponent;
import net.vpc.common.swings.app.Application;

public interface NodeSupplierContext {
    <T> T getParentGuiComponent();

    Application getApplication();

    BindingNodeFactory getFactory();

    default <T> T  createGuiComponent(AppComponent comp) {
        return (T) getFactory().createGuiComponent(getParentGuiComponent(), comp, getApplication());
    }
}
