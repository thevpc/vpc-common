package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.AppComponent;
import net.vpc.common.swings.app.Application;

public interface ToolGuiComponentSupplier {
    Object createGuiElement(Object parentGuiElement, AppComponent appComponent, Application application);
}
