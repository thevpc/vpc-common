package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

public interface AppWindowBuilder {
    WritablePValue<AppLayoutWindowFactory> windowFactory();

    WritablePValue<AppLayoutMenuBarFactory> menuBarFactory();

    WritablePValue<AppLayoutStatusBarFactory> statusBarFactory();

    WritablePValue<AppLayoutToolBarFactory> toolBarFactory();

    WritablePValue<AppLayoutWorkspaceFactory> workspaceFactory();

    AppWindow createWindow(String path, Application application);

}
