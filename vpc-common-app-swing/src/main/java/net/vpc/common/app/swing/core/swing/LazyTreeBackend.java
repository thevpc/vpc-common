package net.vpc.common.app.swing.core.swing;

public interface LazyTreeBackend {
    LazyTreeNode getRoot();

    LazyTreeNode[] getChildren(LazyTreeNode parent);
}
