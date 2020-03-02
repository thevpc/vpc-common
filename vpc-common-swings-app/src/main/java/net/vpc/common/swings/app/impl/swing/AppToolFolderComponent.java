package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.AppComponent;
import net.vpc.common.swings.app.AppToolComponent;
import net.vpc.common.swings.app.AppToolFolder;
import net.vpc.common.swings.app.Application;

import javax.swing.*;

public class AppToolFolderComponent implements ToolGuiComponentSupplier {
    @Override
    public Object createGuiElement(Object parentGuiElement, AppComponent appComponent, Application application) {
        if (appComponent instanceof AppToolComponent) {
            AppToolComponent b = (AppToolComponent) appComponent;
            AppToolFolder tool = (AppToolFolder) b.tool();
            if (parentGuiElement instanceof JStatusBarGroup
            ) {
                JToolBar m = new JToolBar();
                m.setFloatable(false);
                return m;
            }
            if (parentGuiElement instanceof JToolbarGroup
            ) {
                JToolBar m = new JToolBar();
                m.setFloatable(false);
                return m;
            }
            if (parentGuiElement instanceof JToolBar) {
//                JToolBar m = new JToolBar();
//                return m;
            }
            if (parentGuiElement instanceof JMenu || parentGuiElement instanceof JMenuBar) {
                JMenu m = new JMenu();
                SwingHelper.prepareAbstractButton(m, b, application);
                return m;
            }
        }
        return null;
    }
}
