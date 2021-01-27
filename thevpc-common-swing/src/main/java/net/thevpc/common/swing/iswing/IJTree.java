package net.thevpc.common.swing.iswing;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.*;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import java.util.Enumeration;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)  alias vpc
 * %creationtime 2009/08/15 17:38:37
 */
public interface IJTree extends IJComponent {

    //JTree
    public void setCellRenderer(TreeCellRenderer treeCellRenderer);

    public TreePath[] getSelectionPaths();

    public void setSelectionPath(TreePath path);

    public TreeModel getModel();

    public Enumeration getExpandedDescendants(TreePath parent);

    public void setComponentPopupMenu(JPopupMenu popup);

    public void setModel(TreeModel newModel);

    public void makeVisible(TreePath path);

    public void expandPath(TreePath path);

    public void expandRow(int row);

    public void collapsePath(TreePath path);

    public void collapseRow(int row);

    public boolean isExpanded(TreePath path);

    public boolean isExpanded(int row);

    public boolean isCollapsed(TreePath path);

    public boolean isCollapsed(int row);

    public void setDragEnabled(boolean b);

    public boolean getDragEnabled();

    public void setTransferHandler(TransferHandler newHandler);


    public void addTreeExpansionListener(TreeExpansionListener tel);


    public void addTreeSelectionListener(TreeSelectionListener tsl);

    public void removeTreeSelectionListener(TreeSelectionListener tsl);

    public void removeTreeExpansionListener(TreeExpansionListener tel);

    public TreeCellEditor getCellEditor();

    public TreePath getPathForLocation(int x, int y);

}
