package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.*;

import javax.swing.*;

public class AppToolActionComponent implements ToolGuiComponentSupplier{
    @Override
    public Object createGuiElement(Object parentGuiElement, AppComponent appComponent, Application application) {
        if (appComponent instanceof AppToolComponent) {
            AppToolComponent b = (AppToolComponent) appComponent;
            AppToolAction tool = (AppToolAction) b.tool();
            if (parentGuiElement instanceof JToolBar
                    || parentGuiElement instanceof JToolbarGroup
                    || parentGuiElement instanceof JStatusBarGroup
                    || parentGuiElement instanceof JMenuBar
            ) {
                JButton m = new JButton();
                SwingHelper.prepareAbstractButton(m, b, application);
                return m;
            }
            if (parentGuiElement instanceof JMenu) {
                JMenuItem m = new JMenuItem();
                SwingHelper.prepareAbstractButton(m, b, application);
                return m;
            }
        }
        return null;
    }
}
