package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.AppLayoutToolBarFactory;
import net.vpc.common.swings.app.AppToolBar;
import net.vpc.common.swings.app.AppWindow;
import net.vpc.common.swings.app.Application;

import javax.swing.*;

public class JToolbarGroupToolBar extends AppToolContainerImpl implements AppToolBar ,JComponentSupplier{
    public JToolbarGroupToolBar(String rootPath, Application application) {
        super(rootPath, new JToolbarGroup(), application);
    }

    public static AppLayoutToolBarFactory factory() {
        return new AppLayoutToolBarFactory() {
            @Override
            public AppToolBar createToolBar(String path, AppWindow window, Application application) {
                return new JToolbarGroupToolBar(path, application);
            }
        };
    }

    public JComponent component() {
        return (JComponent) super.component();
    }

}
