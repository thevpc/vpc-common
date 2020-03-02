package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.*;
import net.vpc.common.swings.app.core.GuiComponentNavigator;

import javax.swing.*;

class JToolBarGuiComponentNavigator implements GuiComponentNavigator<JToolBar> {

    public JToolBarGuiComponentNavigator() {
    }

    @Override
    public int getItemCount(NodeSupplierContext context) {
        JToolBar parent = context.getParentGuiComponent();
        return parent.getComponentCount();
    }

    @Override
    public Object getItemAt(int index, NodeSupplierContext context) {
        JToolBar parent = context.getParentGuiComponent();
        return parent.getComponent(index);
    }

    @Override
    public Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context) {
        JToolBar parent = context.getParentGuiComponent();
        JComponent item = context.createGuiComponent(b);
        parent.add(item, index);
        return item;
    }
}
