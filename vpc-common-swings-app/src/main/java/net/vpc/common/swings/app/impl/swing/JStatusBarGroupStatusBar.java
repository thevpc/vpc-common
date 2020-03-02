package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.AppLayoutStatusBarFactory;
import net.vpc.common.swings.app.AppStatusBar;
import net.vpc.common.swings.app.AppWindow;
import net.vpc.common.swings.app.Application;

public class JStatusBarGroupStatusBar extends AppToolContainerImpl implements AppStatusBar, JComponentSupplier {
    public JStatusBarGroupStatusBar(String rootPath, Application application) {
        super(rootPath, new JStatusBarGroup(), application);
    }

    public static AppLayoutStatusBarFactory factory() {
        return new AppLayoutStatusBarFactory() {
            @Override
            public AppStatusBar createStatusBar(String path, AppWindow window, Application application) {
                return new JStatusBarGroupStatusBar(path, application);
            }
        };
    }

    public JStatusBarGroup component() {
        return (JStatusBarGroup) super.component();
    }
}
