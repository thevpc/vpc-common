package net.thevpc.common.app.swing.core.swing;

import net.thevpc.common.app.AppToolComponent;
import net.thevpc.common.app.swing.core.GuiComponentNavigator;

import javax.swing.*;

class JComponentGuiComponentNavigator implements GuiComponentNavigator<JComponent> {
    @Override
    public int getItemCount(NodeSupplierContext context) {
        return 0;
    }

    @Override
    public Object getItemAt(int index, NodeSupplierContext context) {
        throw new IllegalArgumentException("Unsupported get item for "+context.getParentGuiComponent().getClass().getSimpleName());
    }

    @Override
    public Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context) {
        throw new IllegalArgumentException("Unsupported add item to "+context.getParentGuiComponent().getClass().getSimpleName());
    }
}