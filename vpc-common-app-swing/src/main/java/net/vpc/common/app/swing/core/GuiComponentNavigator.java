package net.vpc.common.app.swing.core;

import net.vpc.common.app.AppToolComponent;
import net.vpc.common.app.swing.core.swing.NodeSupplierContext;

public interface GuiComponentNavigator<T> {
    int getItemCount(NodeSupplierContext context);

    Object getItemAt(int index, NodeSupplierContext context);

    Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context);
}
