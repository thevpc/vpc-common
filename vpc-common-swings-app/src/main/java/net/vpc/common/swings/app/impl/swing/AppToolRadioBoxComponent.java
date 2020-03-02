package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.*;

import javax.swing.*;

public class AppToolRadioBoxComponent implements ToolGuiComponentSupplier{
    @Override
    public Object createGuiElement(Object parentGuiElement, AppComponent appComponent, Application application) {
        if (appComponent instanceof AppToolComponent) {
            AppToolComponent b = (AppToolComponent) appComponent;
            AppToolRadioBox tool = (AppToolRadioBox) b.tool();
            if (parentGuiElement instanceof JToolBar
                    || parentGuiElement instanceof JToolbarGroup
                    || parentGuiElement instanceof JStatusBarGroup
                    || parentGuiElement instanceof JMenuBar
            ) {
                JRadioButton m = new JRadioButton();
                SwingHelper.prepareAbstractButton(m, b, application);
                return m;
            }
            if (parentGuiElement instanceof JMenu) {
                JRadioButtonMenuItem m = new JRadioButtonMenuItem();
                SwingHelper.prepareAbstractButton(m, b, application);
                return m;
            }
        }
        return null;
    }
}
