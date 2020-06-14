package net.vpc.common.app.swing.core.swing;

import net.vpc.common.app.AppComponent;
import net.vpc.common.app.AppComponentRendererContext;
import net.vpc.common.app.Application;

public interface NodeSupplierContext {
    <T> T getParentGuiComponent();

    Application getApplication();

    BindingNodeFactory getFactory();

    default <T> T  createGuiComponent(AppComponent comp) {
        return (T) getFactory().createGuiComponent(new AppComponentRendererContext(getParentGuiComponent(), comp, getApplication()));
    }
}
