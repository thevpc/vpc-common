package net.vpc.common.app;

public interface AppLayoutStatusBarFactory {
    AppStatusBar createStatusBar(String path, AppWindow window, Application application);
}