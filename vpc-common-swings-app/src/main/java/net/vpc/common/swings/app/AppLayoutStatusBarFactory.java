package net.vpc.common.swings.app;

public interface AppLayoutStatusBarFactory {
    AppStatusBar createStatusBar(String path, AppWindow window, Application application);
}
