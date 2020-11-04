package net.thevpc.common.app.swing.core.swing;


import net.thevpc.common.app.AppLayoutMenuBarFactory;
import net.thevpc.common.app.AppMenuBar;
import net.thevpc.common.app.AppWindow;
import net.thevpc.common.app.Application;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;

import javax.swing.*;

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
