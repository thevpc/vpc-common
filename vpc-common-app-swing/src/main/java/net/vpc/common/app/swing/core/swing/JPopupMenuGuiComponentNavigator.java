/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app.swing.core.swing;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import net.vpc.common.app.AppToolComponent;
import net.vpc.common.app.swing.core.GuiComponentNavigator;

/**
 *
 * @author vpc
 */
class JPopupMenuGuiComponentNavigator implements GuiComponentNavigator<JPopupMenu> {
    
    public JPopupMenuGuiComponentNavigator() {
    }

    @Override
    public int getItemCount(NodeSupplierContext context) {
        JPopupMenu parent = context.getParentGuiComponent();
        return parent.getComponentCount();
    }

    @Override
    public Object getItemAt(int index, NodeSupplierContext context) {
        JPopupMenu parent = context.getParentGuiComponent();
        return parent.getComponent(index);
    }

    @Override
    public Object addChildItem(int index, AppToolComponent b, NodeSupplierContext context) {
        JPopupMenu parent = context.getParentGuiComponent();
        JComponent item = context.createGuiComponent(b);
        parent.add(item, index);
        return item;
    }
    
}