package net.thevpc.common.app.swing.core.swing;

import net.thevpc.common.app.*;
import net.thevpc.common.app.*;

import javax.swing.*;

public class AppToolActionComponent implements AppComponentRenderer {

    @Override
    public Object createGuiComponent(AppComponentRendererContext context) {
        Object parentGuiElement=context.getParentGuiElement();
        AppComponent appComponent=context.getAppComponent();
        Application application=context.getApplication();
        if (appComponent instanceof AppToolComponent) {
            AppToolComponent b = (AppToolComponent) appComponent;
            AppToolAction tool = (AppToolAction) b.tool();
            if (parentGuiElement instanceof JToolBar
                    || parentGuiElement instanceof JToolbarGroup
                    || parentGuiElement instanceof JStatusBarGroup) {
                JButton m = new JButton();
                SwingHelper.prepareAbstractButton(m, b, application, false);
                return m;
            }
            if (parentGuiElement instanceof JMenuBar) {
                JButton m = new JButton();
                SwingHelper.prepareAbstractButton(m, b, application, true);
                return m;
            }
            if (parentGuiElement instanceof JMenu
                    || parentGuiElement instanceof JPopupMenu) {
                JMenuItem m = new JMenuItem();
                SwingHelper.prepareAbstractButton(m, b, application, true);
                return m;
            }
        }
        return null;
    }
}
