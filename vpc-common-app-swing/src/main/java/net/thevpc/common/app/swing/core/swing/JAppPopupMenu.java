package net.thevpc.common.app.swing.core.swing;

import java.awt.Component;

import net.thevpc.common.app.AppLayoutPopupMenuFactory;
import net.thevpc.common.app.AppPopupMenu;
import net.thevpc.common.app.AppWindow;
import net.thevpc.common.app.Application;

import javax.swing.*;

public class JAppPopupMenu extends AppToolContainerImpl implements AppPopupMenu, JPopupMenuComponentSupplier {

    public JAppPopupMenu(String rootPath, Application application) {
        super(rootPath, new JPopupMenu(), application);
    }

    public static AppLayoutPopupMenuFactory factory() {
        return new AppLayoutPopupMenuFactory() {
            @Override
            public AppPopupMenu createPopupMenu(String path, AppWindow window, Application application) {
                return new JAppPopupMenu(path, application);
            }
        };
    }

    public JPopupMenu component() {
        return (JPopupMenu) super.component();
    }

    @Override
    public void show(Object source, int x, int y) {
        component().show((Component) source, x, y);
    }

    @Override
    public boolean isActionable() {
        return _isActionable(component());
    }

    private boolean _isActionable(Object o) {
        if (o instanceof JPopupMenu) {
            JPopupMenu p = (JPopupMenu) o;
            for (MenuElement subElement : p.getSubElements()) {
                if (_isActionable(subElement)) {
                    return true;
                }
            }
        }
        if (o instanceof MenuElement) {
            MenuElement p = (MenuElement) o;
            Component c = p.getComponent();
            if (c.isVisible() && c.isEnabled()) {
                return true;
            }
            for (MenuElement subElement : p.getSubElements()) {
                if (_isActionable(subElement)) {
                    return true;
                }
            }
        }
        return false;
    }

}
