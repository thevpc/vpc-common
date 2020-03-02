package net.vpc.common.swings.app;

import net.vpc.common.swings.app.core.DefaultApplication;
import net.vpc.common.swings.app.impl.swing.*;

import javax.swing.*;

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
            return JMenuBarMenuBar.factory();
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
            return JToolbarGroupToolBar.factory();
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
