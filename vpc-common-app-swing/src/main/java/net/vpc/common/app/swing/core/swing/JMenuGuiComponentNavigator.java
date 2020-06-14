package net.vpc.common.app.swing.core.swing;

import net.vpc.common.app.AppToolComponent;
import net.vpc.common.app.swing.core.GuiComponentNavigator;

import javax.swing.*;

class JMenuGuiComponentNavigator implements GuiComponentNavigator<JMenu> {

    public JMenuGuiComponentNavigator() {

    }

    @Override
    public int getItemCount(NodeSupplierContext context) {
        JMenu parent=context.getParentGuiComponent();
        return parent.getItemCount();
    }

    @Override
    public Object getItemAt(int index, NodeSupplierContext context) {
        JMenu parent=context.getParentGuiComponent();
        return parent.getItem(index);
    }

    @Override
    public Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context) {
        JMenu parent=context.getParentGuiComponent();
        JComponent item = context.createGuiComponent(b);
        parent.add(item, index);
        return item;
    }

}
