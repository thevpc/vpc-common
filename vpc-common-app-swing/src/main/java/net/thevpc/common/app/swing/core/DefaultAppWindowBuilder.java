package net.thevpc.common.app.swing.core;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.common.app.AppWindow;
import net.thevpc.common.app.AppWindowBuilder;
import net.thevpc.common.app.AppToolBar;
import net.thevpc.common.app.Application;
import net.thevpc.common.app.AppWorkspace;
import net.thevpc.common.app.AppLayoutWorkspaceFactory;
import net.thevpc.common.app.AppLayoutToolBarFactory;
import net.thevpc.common.app.AppMenuBar;
import net.thevpc.common.app.AppLayoutStatusBarFactory;
import net.thevpc.common.app.AppLayoutWindowFactory;
import net.thevpc.common.app.AppStatusBar;
import net.thevpc.common.app.AppLayoutMenuBarFactory;

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
            throw new IllegalArgumentException("missing window factory");
        }
        AppWindow window = wf.createWindow(path, application);
        if (window == null) {
            throw new IllegalArgumentException("invalid window factory. returned null window");
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
                window.workspace().set(bar);
            }
        }
        return window;
    }
}
