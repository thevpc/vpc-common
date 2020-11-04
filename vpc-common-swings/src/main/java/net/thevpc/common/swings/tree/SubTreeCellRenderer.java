/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.tree;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author vpc
 */
public interface SubTreeCellRenderer {
     Component getTreeCellRendererComponent(TreeCellRenderer parent,JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus);
}
