package net.thevpc.common.app.swing.core.swing;

import javax.swing.JComponent;

import net.thevpc.common.app.AppLayoutStatusBarFactory;
import net.thevpc.common.app.AppStatusBar;
import net.thevpc.common.app.AppWindow;
import net.thevpc.common.app.Application;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;

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
