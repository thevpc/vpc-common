package net.vpc.common.app.swing;

import net.vpc.common.iconset.IconSet;
import net.vpc.common.app.swing.core.swing.JComponentSupplier;
import net.vpc.common.app.swing.core.swing.JStatusBarGroupStatusBar;
import net.vpc.common.app.swing.core.swing.JAppToolBarGroup;
import net.vpc.common.app.swing.core.swing.JAppMenuBar;
import net.vpc.common.app.swing.core.swing.JFrameAppWindow;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;
import net.vpc.common.app.AppDockingWorkspace;
import net.vpc.common.app.AppLayoutMenuBarFactory;
import net.vpc.common.app.AppLayoutStatusBarFactory;
import net.vpc.common.app.AppLayoutToolBarFactory;
import net.vpc.common.app.AppLayoutWindowFactory;
import net.vpc.common.app.AppLayoutWorkspaceFactory;
import net.vpc.common.app.AppState;
import net.vpc.common.app.AppToolWindow;
import net.vpc.common.app.AppTools;
import net.vpc.common.app.AppWindow;
import net.vpc.common.app.AppWindowDisplayMode;
import net.vpc.common.app.AppWorkspace;
import net.vpc.common.app.Application;
import net.vpc.common.props.PList;
import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.common.app.swing.actions.IconAction;
import net.vpc.common.app.swing.actions.PlafAction;
import net.vpc.common.app.swing.core.DefaultApplication;
import net.vpc.swings.plaf.UIPlaf;
import net.vpc.swings.plaf.UIPlafManager;

public class Applications {

    private Applications() {
    }

    public static class Apps {

        private Apps() {
        }

        public static Application Default() {
            return new DefaultApplication();
        }
    }

    public static class Windows {

        private Windows() {
        }

        public static AppLayoutWindowFactory Default() {
            return JFrameAppWindow.factory();
        }
    }

    public static class MenuBars {

        private MenuBars() {
        }

        public static AppLayoutMenuBarFactory Default() {
            return JAppMenuBar.factory();
        }
    }

    public static class StatusBars {

        private StatusBars() {
        }

        public static AppLayoutStatusBarFactory Default() {
            return JStatusBarGroupStatusBar.factory();
        }
    }

    public static class ToolBars {

        private ToolBars() {
        }

        public static AppLayoutToolBarFactory Default() {
            return JAppToolBarGroup.factory();
        }
    }

    public static class Helper {

        public static void addViewActions(Application application) {
            application.tools().addFolder("/mainWindow/menuBar/View");
            addViewToolActions(application);
            addViewPlafActions(application);
            addViewIconActions(application);
            addViewAppearanceActions(application);
        }

        public static void addViewToolActions(Application application) {
            addToolActions(application, "/mainWindow/menuBar/View/Tool Windows");
        }

        public static void addToolActions(Application application, String path) {
            AppTools tools = application.tools();
            tools.addFolder(path);
            runAfterStart(application, () -> {
                AppWorkspace ws = application.mainWindow().get().workspace().get();
                if (ws != null && ws instanceof AppDockingWorkspace) {
                    AppDockingWorkspace dws = (AppDockingWorkspace) ws;
                    PList<AppToolWindow> values = dws.toolWindows().values();
                    java.util.List<AppToolWindow> list = new ArrayList<>();
                    for (AppToolWindow value : values) {
                        list.add(value);
                    }
                    list.sort((a, b) -> a.id().compareTo(b.id()));
                    for (AppToolWindow value : list) {
                        tools.addCheck("ToolWindow-" + value.id(), value.active(), path + "/" + value.id());
                    }
                }
            });

        }

        public static void addViewIconActions(Application application) {
            addIconActions(application, "/mainWindow/menuBar/View/Icons");
        }

        public static void addIconActions(Application application, String path) {
            AppTools tools = application.tools();
            tools.addFolder(path);
            for (IconSet iconset : application.iconSets().values()) {
                Dimension d = iconset.getSize();
                if (d != null) {
                    if (d.width <= 16 && d.height <= 16) {
                        tools.addAction(new IconAction(application, iconset.getId()), path + "/Small/" + iconset.getId());
                    } else if (d.width <= 32 && d.height <= 32) {
                        tools.addAction(new IconAction(application, iconset.getId()), path + "/Large/" + iconset.getId());
                    } else {
                        tools.addAction(new IconAction(application, iconset.getId()), path + "/Huge/" + iconset.getId());
                    }
                } else {
                    tools.addAction(new IconAction(application, iconset.getId()), path + "/Custom/" + iconset.getId());
                }
            }
        }

        public static void addViewPlafActions(Application application) {
            addPlafActions(application, "/mainWindow/menuBar/View/Plaf");
        }

        public static void addPlafActions(Application application, String path) {
            AppTools tools = application.tools();
            tools.addFolder(path);
            for (UIPlaf item : UIPlafManager.INSTANCE.items()) {
                String q = "Other";
                if (item.isSystem()) {
                    q = "System";
                } else if (item.isDark()) {
                    q = "Dark";
                } else if (item.isLight()) {
                    q = "Light";
                } else {
                    q = "Other";
                }
                String name = item.getName();
                String pname = name.replace("/", "_");
                tools.addAction(new PlafAction(application, name), path + "/" + q + "/" + pname);
                if (item.isContrast()) {
                    q = "Contrast";
                    tools.addAction(new PlafAction(application, name), path + "/" + q + "/" + pname);
                }
            }
        }

        public static void addViewAppearanceActions(Application application) {
            addAppearanceActions(application, "/mainWindow/menuBar/View/Appearance");
        }

        public static void addAppearanceActions(Application application, String path) {
            AppTools tools = application.tools();

            runAfterStart(application, () -> {
                tools.addRadio("Normal", "MainWindowDisplayMode", application.mainWindow().get().displayMode(), AppWindowDisplayMode.NORMAL, path + "/Normal");
                tools.addRadio("FullScreen", "MainWindowDisplayMode", application.mainWindow().get().displayMode(), AppWindowDisplayMode.FULLSCREEN, path + "/FullScreen");
                tools.addSeparator(path + "/Separator1");
                tools.addCheck(null, application.mainWindow().get().toolBar().get().visible(), path + "/Toolbar");
                tools.addCheck(null, application.mainWindow().get().statusBar().get().visible(), path + "/StatusBar");
            });
        }

        public static void runAfterStart(Application application, Runnable r) {
            if (application.state().get().ordinal() >= AppState.STARTED.ordinal()) {
                r.run();
            } else {
                application.state().listeners().add(new PropertyListener() {
                    @Override
                    public void propertyUpdated(PropertyEvent event) {
                        AppState e = event.getNewValue();
                        if (e == AppState.STARTED) {
                            r.run();
                        }
                    }
                });
            }
        }

    }

    public static class Workspaces {

        private Workspaces() {
        }

        public static AppLayoutWorkspaceFactory Default() {
            return new DefaultAppLayoutWorkspaceFactory(new JPanel());
        }

        public static AppLayoutWorkspaceFactory Default(JComponent component) {
            return new DefaultAppLayoutWorkspaceFactory(component == null ? new JPanel() : component);
        }

        public static ComponentAppWorkspace Component(JComponent component) {
            return new ComponentAppWorkspace(component == null ? new JPanel() : component);
        }

    }

    private static class DefaultAppLayoutWorkspaceFactory implements AppLayoutWorkspaceFactory {

        private JComponent component;

        public DefaultAppLayoutWorkspaceFactory(JComponent component) {
            this.component = component;
        }

        @Override
        public AppWorkspace createWorkspace(AppWindow window) {
            return new ComponentAppWorkspace(component);
        }
    }

    private static class ComponentAppWorkspace implements AppWorkspace, JComponentSupplier {

        private JComponent component;

        public ComponentAppWorkspace(JComponent component) {
            this.component = component;
        }

        @Override
        public JComponent component() {
            return component;
        }
    }
}
