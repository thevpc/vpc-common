package net.thevpc.common.app;

public interface AppLayoutStatusBarFactory {
    AppStatusBar createStatusBar(String path, AppWindow window, Application application);
}
