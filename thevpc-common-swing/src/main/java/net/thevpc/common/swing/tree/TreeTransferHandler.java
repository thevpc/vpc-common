/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

/**
 *
 * @author vpc
 */
public class TreeTransferHandler extends TransferHandler {

    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[2];
    Object[] nodesToRemove;
    AbstractTreeModel treeModel;
    Class clz;

    public TreeTransferHandler(Class clz, AbstractTreeModel treeModel) {
        this.treeModel = treeModel;
        this.clz = clz;
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=\""
                    + Array.newInstance(clz, 0).getClass().getName()
                    + "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
            flavors[1] = DataFlavor.stringFlavor;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    private boolean isSupportDataFlavorSupported(TransferHandler.TransferSupport support) {
        for (DataFlavor flavor : flavors) {
            if (support.isDataFlavorSupported(flavor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if (!isSupportDataFlavorSupported(support)) {
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl
                = (JTree.DropLocation) support.getDropLocation();
        JTree tree = (JTree) support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for (int i = 0; i < selRows.length; i++) {
            if (selRows[i] == dropRow) {
                return false;
            }
        }
        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        int action = support.getDropAction();
        if (action == TransferHandler.MOVE) {
            return haveCompleteNode(tree);
        }
        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
        TreePath dest = dl.getPath();
        if (dest == null) {
//            Object target= null;
//            TreePath path = tree.getPathForRow(selRows[0]);
//            Object firstNode
//                    = (Object) path.getLastPathComponent();
//            if (treeModel.getChildCount(firstNode) > 0
//                    && treeModel.getLevel(target) < treeModel.getLevel(firstNode)) {
//                return false;
//            }
            return true;
        } else {
            if(selRows.length==0){
                return true;
            }
            Object target
                    = dest.getLastPathComponent();
            TreePath path = tree.getPathForRow(selRows[0]);
            Object firstNode
                    = (Object) path.getLastPathComponent();
            if (treeModel.getChildCount(firstNode) > 0
                    && treeModel.getLevel(target) < treeModel.getLevel(firstNode)) {
                return false;
            }
            return true;
        }
    }

    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        Object first
                = (Object) path.getLastPathComponent();
        int childCount = treeModel.getChildCount(first);
        // first has children and no children are selected.
//        if (childCount > 0 && selRows.length == 1) {
//            return false;
//        }
        // first may have children.
        for (int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            Object next = path.getLastPathComponent();
            if (treeModel.isNodeChild(first, next)) {
                // Found a child of first.
                if (childCount > selRows.length - 1) {
                    // Not all children of first are selected.
                    return false;
                }
            }
        }
        return true;
    }

    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<Object> copies
                    = new ArrayList<Object>();
            List<Object> toRemove
                    = new ArrayList<Object>();
            Object node
                    = (Object) paths[0].getLastPathComponent();
            Object copy = treeModel.copyNode(node);
            copies.add(copy);
            toRemove.add(node);
            for (int i = 1; i < paths.length; i++) {
                Object next
                        = (Object) paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if (treeModel.getLevel(next) < treeModel.getLevel(node)) {
                    break;
                } else if (treeModel.getLevel(next) > treeModel.getLevel(node)) {  // child node
                    treeModel.insertNodeInto(treeModel.copyNode(next), copy, treeModel.getChildCount(copy));
                    // node already contains child
                } else {                                        // sibling
                    copies.add(treeModel.copyNode(next));
                    toRemove.add(next);
                }
            }
            Object[] nodes
                    = copies.toArray((Object[]) Array.newInstance(clz, copies.size()));
            nodesToRemove
                    = toRemove.toArray(new Object[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        if ((action & MOVE) == MOVE) {
            JTree tree = (JTree) source;
            AbstractTreeModel model = (AbstractTreeModel) tree.getModel();
            // Remove nodes saved in nodesToRemove in createTransferable.
            for (int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
    }

    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    public Object getTransferable(Transferable t) {
        Reader reader = null;
        for (DataFlavor flavor : flavors) {
            try {
                Object o = t.getTransferData(flavor);
                if (o != null) {
                    return o;
                }
            } catch (UnsupportedFlavorException ufe) {
                System.out.println("UnsupportedFlavor: " + ufe.getMessage());
            } catch (java.io.IOException ioe) {
                System.out.println("I/O error: " + ioe.getMessage());
            }
        }
        try {
            DataFlavor a = DataFlavor.selectBestTextFlavor(t.getTransferDataFlavors());
            reader = a.getReaderForText(t);
            char[] buffer = new char[4096];
            StringBuilder builder = new StringBuilder();
            int numChars;
            while ((numChars = reader.read(buffer)) >= 0) {
                builder.append(buffer, 0, numChars);
            }
            String str = builder.toString();
            return str;
        } catch (UnsupportedFlavorException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                //
            }
        }
        return null;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        // Extract transfer data.
        Object[] nodes = null;
        Transferable t = support.getTransferable();
        Object a = getTransferable(t);
        if (a instanceof Object[]) {
            nodes = (Object[]) a;
        } else if (a instanceof String) {
            nodes = new Object[]{a};
        } else if (a != null) {
            nodes = new Object[]{a};
        } else {
            return false;
        }

        // Get drop location info.
        JTree.DropLocation dl
                = (JTree.DropLocation) support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        Object parent = dest.getLastPathComponent();
        JTree tree = (JTree) support.getComponent();
        AbstractTreeModel model = (AbstractTreeModel) tree.getModel();
        // Configure for drop mode.
        int index = childIndex;    // DropMode.INSERT
        if (childIndex == -1) {     // DropMode.ON
            index = treeModel.getChildCount(parent);
        }
        // Add data to model.
        for (int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {

        Object[] nodes;

        public NodesTransferable(Object[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}
