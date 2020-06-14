package net.vpc.common.app.swing.core.swing;

import net.vpc.common.app.AppLayoutToolBarFactory;
import net.vpc.common.app.AppToolBar;
import net.vpc.common.app.AppWindow;
import net.vpc.common.app.Application;

import javax.swing.*;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;

public class JAppToolBarGroup extends AppToolContainerImpl implements AppToolBar, JComponentSupplier {

    private WritablePValue<Boolean> visible = Props.of("visible").valueOf(Boolean.class, false);

    public JAppToolBarGroup(String rootPath, Application application) {
        super(rootPath, new JToolbarGroup(), application);
        SwingHelper.bindVisible((JComponent) rootGuiElement, visible);
    }

    @Override
    public WritablePValue<Boolean> visible() {
        return visible;
    }

    public static AppLayoutToolBarFactory factory() {
        return new AppLayoutToolBarFactory() {
            @Override
            public AppToolBar createToolBar(String path, AppWindow window, Application application) {
                return new JAppToolBarGroup(path, application);
            }
        };
    }

    public JComponent component() {
        return (JComponent) super.component();
    }

}
