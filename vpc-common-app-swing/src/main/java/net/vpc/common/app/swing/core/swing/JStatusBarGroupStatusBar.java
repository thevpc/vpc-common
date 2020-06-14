package net.vpc.common.app.swing.core.swing;

import javax.swing.JComponent;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.app.AppLayoutStatusBarFactory;
import net.vpc.common.app.AppStatusBar;
import net.vpc.common.app.AppWindow;
import net.vpc.common.app.Application;

public class JStatusBarGroupStatusBar extends AppToolContainerImpl implements AppStatusBar, JComponentSupplier {

    private WritablePValue<Boolean> visible = Props.of("visible").valueOf(Boolean.class, false);

    public JStatusBarGroupStatusBar(String rootPath, Application application) {
        super(rootPath, new JStatusBarGroup(), application);
        SwingHelper.bindVisible((JComponent) rootGuiElement, visible);
    }

    @Override
    public WritablePValue<Boolean> visible() {
        return visible;
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
