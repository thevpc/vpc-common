package net.vpc.common.app;

public interface AppLayoutMenuBarFactory {
    AppMenuBar createMenuBar(String path, AppWindow window, Application application);
}
