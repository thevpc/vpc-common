package net.thevpc.common.app.swing.core.swing;

public interface LazyTreeBackend {
    LazyTreeNode getRoot();

    LazyTreeNode[] getChildren(LazyTreeNode parent);
}
