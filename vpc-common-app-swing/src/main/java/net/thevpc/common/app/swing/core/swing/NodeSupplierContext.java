package net.thevpc.common.app.swing.core.swing;

import net.thevpc.common.app.AppComponent;
import net.thevpc.common.app.AppComponentRendererContext;
import net.thevpc.common.app.Application;

public interface NodeSupplierContext {
    <T> T getParentGuiComponent();

    Application getApplication();

    BindingNodeFactory getFactory();

    default <T> T  createGuiComponent(AppComponent comp) {
        return (T) getFactory().createGuiComponent(new AppComponentRendererContext(getParentGuiComponent(), comp, getApplication()));
    }
}
