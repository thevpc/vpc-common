package net.thevpc.common.app.swing.core.swing;

import net.thevpc.common.app.AppToolComponent;
import net.thevpc.common.app.swing.core.GuiComponentNavigator;

import javax.swing.*;

class JStatusBarGroupGuiComponentNavigator implements GuiComponentNavigator<JStatusBarGroup> {
    public JStatusBarGroupGuiComponentNavigator() {
    }

    @Override
    public int getItemCount(NodeSupplierContext context) {
        JStatusBarGroup parent=context.getParentGuiComponent();
        return parent.getComponentCount();
    }

    @Override
    public Object getItemAt(int index, NodeSupplierContext context) {
        JStatusBarGroup parent=context.getParentGuiComponent();
        return parent.getComponent(index);
    }

    @Override
    public Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context) {
        JStatusBarGroup parent=context.getParentGuiComponent();
        JComponent item = context.createGuiComponent(b);
        parent.add(item, index);
        return item;
    }
}