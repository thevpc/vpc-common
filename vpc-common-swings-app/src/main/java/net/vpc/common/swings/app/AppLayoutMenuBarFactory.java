package net.vpc.common.swings.app;

public interface AppLayoutMenuBarFactory {
    AppMenuBar createMenuBar(String path, AppWindow window, Application application);
}
