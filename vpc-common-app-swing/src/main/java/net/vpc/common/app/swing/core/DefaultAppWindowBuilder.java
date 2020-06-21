package net.vpc.common.app.swing.core;

import net.vpc.common.app.AppWindow;
import net.vpc.common.app.AppWindowBuilder;
import net.vpc.common.app.AppToolBar;
import net.vpc.common.app.Application;
import net.vpc.common.app.AppWorkspace;
import net.vpc.common.app.AppLayoutWorkspaceFactory;
import net.vpc.common.app.AppLayoutToolBarFactory;
import net.vpc.common.app.AppMenuBar;
import net.vpc.common.app.AppLayoutStatusBarFactory;
import net.vpc.common.app.AppLayoutWindowFactory;
import net.vpc.common.app.AppStatusBar;
import net.vpc.common.app.AppLayoutMenuBarFactory;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;

public class DefaultAppWindowBuilder implements AppWindowBuilder {
    private final WritablePValue<AppLayoutWindowFactory> windowFactory = Props.of("windowFactory").valueOf(AppLayoutWindowFactory.class, null);
    private final WritablePValue<AppLayoutMenuBarFactory> menuBarFactory = Props.of("menuBarFactory").valueOf(AppLayoutMenuBarFactory.class, null);
    private final WritablePValue<AppLayoutStatusBarFactory> statusBarFactory = Props.of("statusBarFactory").valueOf(AppLayoutStatusBarFactory.class, null);
    private final WritablePValue<AppLayoutToolBarFactory> toolBarFactory = Props.of("toolBarFactory").valueOf(AppLayoutToolBarFactory.class, null);
    private final WritablePValue<AppLayoutWorkspaceFactory> workspaceFactory = Props.of("workspaceFactory").valueOf(AppLayoutWorkspaceFactory.class, null);

    @Override
    public WritablePValue<AppLayoutWindowFactory> windowFactory() {
        return windowFactory;
    }

    @Override
    public WritablePValue<AppLayoutMenuBarFactory> menuBarFactory() {
        return menuBarFactory;
    }

    @Override
    public WritablePValue<AppLayoutStatusBarFactory> statusBarFactory() {
        return statusBarFactory;
    }

    @Override
    public WritablePValue<AppLayoutToolBarFactory> toolBarFactory() {
        return toolBarFactory;
    }

    @Override
    public WritablePValue<AppLayoutWorkspaceFactory> workspaceFactory() {
        return workspaceFactory;
    }

    @Override
    public AppWindow createWindow(String path, Application application) {
        AppLayoutWindowFactory wf = windowFactory().get();
        if (wf == null) {
            throw new IllegalArgumentException("Missing Window Factory");
        }
        AppWindow window = wf.createWindow(path, application);
        if (window == null) {
            throw new IllegalArgumentException("Invalid Window Factory. Returned Null Window");
        }

        AppLayoutMenuBarFactory m = menuBarFactory().get();
        if (m != null) {
            AppMenuBar bar = m.createMenuBar("/menuBar", window, application);
            if (bar != null) {
                window.menuBar().set(bar);
            }
        }
        AppLayoutStatusBarFactory s = statusBarFactory().get();
        if (s != null) {
            AppStatusBar bar = s.createStatusBar("/statusBar", window, application);
            if (bar != null) {
                window.statusBar().set(bar);
            }
        }
        AppLayoutToolBarFactory t = toolBarFactory().get();
        if (t != null) {
            AppToolBar bar = t.createToolBar("/toolBar", window, application);
            if (bar != null) {
                window.toolBar().set(bar);
            }
        }
        AppLayoutWorkspaceFactory w = workspaceFactory().get();
        if (w != null) {
            AppWorkspace bar = w.createWorkspace(window);
            if (bar != null) {
                window.getWorkspace().set(bar);
            }
        }
        return window;
    }
}
