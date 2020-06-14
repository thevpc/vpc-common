package net.vpc.common.app.swing.core.swing;

import net.vpc.common.app.AppToolComponent;
import net.vpc.common.app.swing.core.GuiComponentNavigator;

import javax.swing.*;

public class JMenuBarGuiComponentNavigator implements GuiComponentNavigator<JMenuBar> {

    public JMenuBarGuiComponentNavigator() {
    }

    @Override
    public int getItemCount(NodeSupplierContext context) {
        JMenuBar parent=context.getParentGuiComponent();
        return parent.getMenuCount();
    }

    @Override
    public Object getItemAt(int index, NodeSupplierContext context) {
        JMenuBar parent=context.getParentGuiComponent();
        return parent.getMenu(index);
    }

    @Override
    public Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context) {
        JMenuBar parent=context.getParentGuiComponent();
        JComponent item = context.createGuiComponent(b);
        parent.add(item, index);
        return item;
    }
}
