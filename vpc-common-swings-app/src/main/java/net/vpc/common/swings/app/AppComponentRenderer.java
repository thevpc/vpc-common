package net.vpc.common.swings.app;

public interface AppComponentRenderer {
    Object createGuiComponent(AppComponent appComponent, Object parentGuiElement, Application application);
}
