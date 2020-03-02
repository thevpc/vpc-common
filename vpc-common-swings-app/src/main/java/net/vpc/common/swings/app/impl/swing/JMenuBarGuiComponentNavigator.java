package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.AppToolComponent;
import net.vpc.common.swings.app.core.GuiComponentNavigator;

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
