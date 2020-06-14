package net.vpc.common.app.swing.core.swing;


import net.vpc.common.app.AppLayoutMenuBarFactory;
import net.vpc.common.app.AppMenuBar;
import net.vpc.common.app.AppWindow;
import net.vpc.common.app.Application;

import javax.swing.*;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;

public class JAppMenuBar extends AppToolContainerImpl implements AppMenuBar,JMenuBarComponentSupplier {
    private WritablePValue<Boolean> visible = Props.of("visible").valueOf(Boolean.class, false);
    public JAppMenuBar(String rootPath, Application application) {
        super(rootPath, new JMenuBar(), application);
        SwingHelper.bindVisible((JComponent)rootGuiElement, visible);
    }

    public static AppLayoutMenuBarFactory factory() {
        return new AppLayoutMenuBarFactory() {
            @Override
            public AppMenuBar createMenuBar(String path, AppWindow window, Application application) {
                return new JAppMenuBar(path, application);
            }
        };
    }
    
     @Override
    public WritablePValue<Boolean> visible() {
        return visible;
    }

    public JMenuBar component() {
        return (JMenuBar) super.component();
    }
}
