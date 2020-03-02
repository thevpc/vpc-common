package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.*;

import javax.swing.*;

public class AppToolCheckBoxComponent implements ToolGuiComponentSupplier{
    @Override
    public Object createGuiElement(Object parentGuiElement, AppComponent appComponent, Application application) {
        if (appComponent instanceof AppToolComponent) {
            AppToolComponent b = (AppToolComponent) appComponent;
            AppToolCheckBox tool = (AppToolCheckBox) b.tool();
            if (parentGuiElement instanceof JToolBar
                    || parentGuiElement instanceof JToolbarGroup
                    || parentGuiElement instanceof JStatusBarGroup
                    || parentGuiElement instanceof JMenuBar
            ) {
                JCheckBox m = new JCheckBox();
                SwingHelper.prepareAbstractButton(m, b, application);
                return m;
            }
            if (parentGuiElement instanceof JMenu) {
                JCheckBoxMenuItem m = new JCheckBoxMenuItem();
                SwingHelper.prepareAbstractButton(m, b, application);
                return m;
            }
        }
        return null;
    }
}
