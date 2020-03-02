package net.vpc.common.swings.app.impl.swing;


import net.vpc.common.swings.app.AppLayoutMenuBarFactory;
import net.vpc.common.swings.app.AppMenuBar;
import net.vpc.common.swings.app.AppWindow;
import net.vpc.common.swings.app.Application;

import javax.swing.*;

public class JMenuBarMenuBar extends AppToolContainerImpl implements AppMenuBar,JMenuBarComponentSupplier {
    public JMenuBarMenuBar(String rootPath, Application application) {
        super(rootPath, new JMenuBar(), application);
    }

    public static AppLayoutMenuBarFactory factory() {
        return new AppLayoutMenuBarFactory() {
            @Override
            public AppMenuBar createMenuBar(String path, AppWindow window, Application application) {
                return new JMenuBarMenuBar(path, application);
            }
        };
    }

    public JMenuBar component() {
        return (JMenuBar) super.component();
    }
}
