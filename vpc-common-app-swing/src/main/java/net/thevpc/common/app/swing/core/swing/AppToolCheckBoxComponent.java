package net.thevpc.common.app.swing.core.swing;

import net.thevpc.common.app.*;
import net.thevpc.common.app.*;

import javax.swing.*;

public class AppToolCheckBoxComponent implements AppComponentRenderer {

    @Override
    public Object createGuiComponent(AppComponentRendererContext context) {
        Object parentGuiElement=context.getParentGuiElement();
        AppComponent appComponent=context.getAppComponent();
        Application application=context.getApplication();
        if (appComponent instanceof AppToolComponent) {
            AppToolComponent b = (AppToolComponent) appComponent;
            AppToolCheckBox tool = (AppToolCheckBox) b.tool();
            if (parentGuiElement instanceof JToolBar
                    || parentGuiElement instanceof JToolbarGroup
                    || parentGuiElement instanceof JStatusBarGroup) {
                JToggleButton m = new JToggleButton();
                SwingHelper.prepareAbstractButton(m, b, application, false);
                return m;
            }
            if (parentGuiElement instanceof JMenuBar) {
                JCheckBox m = new JCheckBox();
                SwingHelper.prepareAbstractButton(m, b, application, true);
                return m;
            }
            if (parentGuiElement instanceof JMenu
                    || parentGuiElement instanceof JPopupMenu) {
                JCheckBoxMenuItem m = new JCheckBoxMenuItem();
                SwingHelper.prepareAbstractButton(m, b, application, true);
                return m;
            }
        }
        return null;
    }
}