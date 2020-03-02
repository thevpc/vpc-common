package net.vpc.common.swings.app.core;

import net.vpc.common.swings.app.AppToolComponent;
import net.vpc.common.swings.app.impl.swing.NodeSupplierContext;

public interface GuiComponentNavigator<T> {
    int getItemCount(NodeSupplierContext context);

    Object getItemAt(int index, NodeSupplierContext context);

    Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context);
}
